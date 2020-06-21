package at.sunilson.authentication.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.GigyaService
import at.sunilson.authentication.data.KamereonService
import at.sunilson.authentication.data.AuthSharedPrefConstants.GIGYA_JWT
import at.sunilson.authentication.data.AuthSharedPrefConstants.GIGYA_PERSON_ID
import at.sunilson.authentication.data.AuthSharedPrefConstants.GIGYA_TOKEN
import at.sunilson.authentication.data.AuthSharedPrefConstants.KAMEREON_ACCESS_TOKEN
import at.sunilson.authentication.data.AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID
import at.sunilson.authentication.data.AuthSharedPrefConstants.LAST_SUCCESSFUL_LOGIN
import at.sunilson.authentication.data.networkEntities.KamereonHeader
import at.sunilson.authentication.domain.entities.LoginParams
import at.sunilson.authentication.domain.exceptions.LoginException
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.networkingcore.constants.ApiKeys.GIGYA_API_KEY
import at.sunilson.networkingcore.constants.ApiKeys.KAMEREON_API_KEY
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.kittinunf.result.coroutines.mapError
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val gigyaService: GigyaService,
    private val kamereonService: KamereonService,
    private val sp: SharedPreferences
) : AsyncUseCase<Unit, LoginParams?>() {

    /**
     * Logs the given user in. If [params] is null this usecase tries to refresh the existing users login
     */
    override suspend fun run(params: LoginParams?) = SuspendableResult.of<Unit, Exception> {
        val lastGigyaToken = sp.getString(GIGYA_TOKEN, null)
        val isSameUser =
            params == null || sp.getString(LAST_SUCCESSFUL_LOGIN, "") == params.username

        if(lastGigyaToken == null && params == null) {
            TODO("Login not possible, throw error")
        }

        val gigyaToken = if (isSameUser && lastGigyaToken != null) {
            lastGigyaToken
        } else {
            requireNotNull(params)
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

        val kamereonAccessToken = kamereonService.kamereonTokens(
            kamereonAccountId,
            KamereonHeader(gigyaJWT, KAMEREON_API_KEY)
        ).accessToken
        sp.edit { putString(KAMEREON_ACCESS_TOKEN, kamereonAccessToken) }

        if (params != null) {
            sp.edit { putString(LAST_SUCCESSFUL_LOGIN, params.username) }
        }
    }

    companion object {
        const val LOG_TAG = "LoginUseCase"
    }
}