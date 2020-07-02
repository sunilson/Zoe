package at.sunilson.authentication.data

import android.content.SharedPreferences
import at.sunilson.authentication.domain.LoginUseCase
import at.sunilson.authentication.domain.LogoutHandler
import at.sunilson.authentication.domain.extensions.isValidJWT
import at.sunilson.networkingcore.constants.ApiKeys
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val loginUseCase: LoginUseCase,
    private val logoutHandler: LogoutHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (gigyaJWT == null || gigyaJWT?.isValidJWT() != true) {
            refreshTokens()
        }

        if (gigyaJWT == null) {
            TODO("Error")
        }

        var result = chain.proceed(createRequest(chain))

        if (!result.isSuccessful && result.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            refreshTokens()
            result.close()
            result = chain.proceed(createRequest(chain))
        }

        if (!result.isSuccessful && result.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            logoutHandler.emitLogout()
        }

        return result
    }


    private fun refreshTokens() = runBlocking {
        loginUseCase(null)
    }

    private fun createRequest(originalChain: Interceptor.Chain) =
        originalChain.request().newBuilder().apply {
            addHeader("Content-Type", "application/vnd.api+json")
            addHeader("apikey", ApiKeys.KAMEREON_API_KEY)
            addHeader("x-gigya-id_token", requireNotNull(gigyaJWT))
        }.build()

    private val gigyaJWT: String?
        get() = sharedPreferences.getString(AuthSharedPrefConstants.GIGYA_JWT, null)
}