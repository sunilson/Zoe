package at.sunilson.authentication.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.GigyaService
import at.sunilson.authentication.data.KamereonService
import at.sunilson.authentication.data.networkEntities.KamereonHeader
import at.sunilson.authentication.domain.entities.LoginParams
import at.sunilson.core.extensions.doOnFailure
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val gigyaService: GigyaService,
    private val kamereonService: KamereonService,
    private val sp: SharedPreferences
) : AsyncUseCase<Unit, LoginParams>() {

    override suspend fun run(params: LoginParams) = SuspendableResult.of<Unit, Exception> {
        val isSameUser = sp.getString(LAST_SUCCESSFUL_LOGIN, "") == params.username

        val lastGigyaToken = sp.getString(GIGYA_TOKEN, null)
        val gigyaToken = if (isSameUser && lastGigyaToken != null) {
            lastGigyaToken
        } else {
            gigyaService.gigyaLogin(
                GIGYA_API_KEY,
                params.username,
                params.password
            ).sessionInfo.cookieValue
        }
        sp.edit { putString(GIGYA_TOKEN, gigyaToken) }

        val lastPersonId = sp.getString(GIGYA_PERSON_ID, null)
        val gigyaPersonId = if (isSameUser && lastPersonId != null) {
            lastPersonId
        } else {
            gigyaService.gigyaAccountInfo(gigyaToken).data.personId
        }
        sp.edit { putString(GIGYA_PERSON_ID, gigyaPersonId) }

        val gigyaJWT =
            gigyaService.gigyaJWT(gigyaToken, "data.personId,data.gigyaDataCenter").idToken
        sp.edit { putString(GIGYA_JWT, gigyaJWT) }

        val lastKamereonAccountId = sp.getString(KAMEREON_ACCOUNT_ID, null)
        val kamereonAccountId = if (isSameUser && lastKamereonAccountId != null) {
            lastKamereonAccountId
        } else {
            kamereonService.kamereonAccount(
                gigyaPersonId,
                KamereonHeader(gigyaJWT, KAMEREON_API_KEY)
            ).accounts.first().accountId
        }
        sp.edit { putString(KAMEREON_ACCOUNT_ID, kamereonAccountId) }
        //TODO Save ID + any other account info that could be of value for app

        kamereonService.kamereonTokens(
            kamereonAccountId,
            KamereonHeader(gigyaJWT, KAMEREON_API_KEY)
        )
        //TODO Save these tokens for future requests

        sp.edit { putString(LAST_SUCCESSFUL_LOGIN, params.username) }
    }.doOnFailure {
        //TODO Logout
    }

    companion object {
        const val GIGYA_TOKEN = "gigyaToken"
        const val GIGYA_PERSON_ID = "gigyaPersonId"
        const val GIGYA_JWT = "gigyaJWT"
        const val KAMEREON_ACCOUNT_ID = "kamereonAccountId"
        const val KAMEREON_ACCESS_TOKEN = "kamereonAccessToken"
        const val LAST_SUCCESSFUL_LOGIN = "lastSuccessfulLogin"

        const val LOG_TAG = "LoginUseCase"
        const val GIGYA_API_KEY =
            "3__B4KghyeUb0GlpU62ZXKrjSfb7CPzwBS368wioftJUL5qXE0Z_sSy0rX69klXuHy"
        const val KAMEREON_API_KEY = "oF09WnKqvBDcrQzcW1rJNpjIuy7KdGaB"
    }

}