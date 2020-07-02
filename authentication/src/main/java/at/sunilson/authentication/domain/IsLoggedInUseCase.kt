package at.sunilson.authentication.domain

import android.content.SharedPreferences
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.core.usecases.UseCase
import com.github.kittinunf.result.Result
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(private val sharedPreferences: SharedPreferences) :
    UseCase<Boolean, Unit>() {
    override fun run(params: Unit) = Result.of<Boolean, Exception> {
        sharedPreferences.getString(
            AuthSharedPrefConstants.GIGYA_JWT,
            null
        ) != null && sharedPreferences.getString(
            AuthSharedPrefConstants.GIGYA_TOKEN,
            null
        ) != null
    }
}