package at.sunilson.authentication.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.core.usecases.UseCase
import com.github.kittinunf.result.Result
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val logoutHandler: LogoutHandler
) : UseCase<Unit, Unit>() {
    override fun run(params: Unit) = Result.of<Unit, Exception> {
        sharedPreferences.edit {
            remove(AuthSharedPrefConstants.LAST_SUCCESSFUL_LOGIN)
            remove(AuthSharedPrefConstants.GIGYA_JWT)
            remove(AuthSharedPrefConstants.GIGYA_TOKEN)
            remove(AuthSharedPrefConstants.GIGYA_PERSON_ID)
            remove(AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID)
        }

        logoutHandler.emitLogout()
    }
}
