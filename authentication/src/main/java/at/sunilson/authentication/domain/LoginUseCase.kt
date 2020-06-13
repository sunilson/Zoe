package at.sunilson.authentication.domain

import android.util.Log
import at.sunilson.authentication.data.GigyaService
import at.sunilson.authentication.data.KamereonService
import at.sunilson.authentication.domain.entities.LoginParams
import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult

class LoginUseCase(
    private val gigyaService: GigyaService,
    private val kamereonService: KamereonService
) : AsyncUseCase<Unit, LoginParams>() {

    override suspend fun run(params: LoginParams) = SuspendableResult.of<Unit, Exception> {
        val gigyaToken = gigyaService.gigyaLogin(
            GIGYA_API_KEY,
            params.username,
            params.password
        ).sessionInfo.cookieValue

        Log.d(LOG_TAG, "Gigya Token: $gigyaToken")

        val gigyaPersonId = gigyaService.gigyaAccountInfo(gigyaToken).data.personId

        Log.d(LOG_TAG, "Gigya Person ID: $gigyaPersonId")

        val gigyaJWT = gigyaService.gigyaJWT(gigyaToken, "data.personId,data.gigyaDataCenter")

        Log.d(LOG_TAG, "Gigya JWT: $gigyaJWT")

        val kamereonAccountId =
            kamereonService.kamereonAccount(gigyaPersonId, gigyaJWT, KAMEREON_API_KEY)

        Log.d(LOG_TAG, "Kamereon Account Id: $kamereonAccountId")
    }

    companion object {
        const val LOG_TAG = "LoginUseCase"
        const val GIGYA_API_KEY = ""
        const val KAMEREON_API_KEY = ""
    }

}