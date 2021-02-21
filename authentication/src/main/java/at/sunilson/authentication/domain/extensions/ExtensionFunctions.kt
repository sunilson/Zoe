package at.sunilson.authentication.domain.extensions

import com.auth0.android.jwt.JWT
import java.time.Instant

/**
 * @return true if jwt is valid and will not expire in the next 5 minutes
 */
fun String.isValidJWT(): Boolean {
    val jwt = JWT(this)
    return jwt.expiresAt?.toInstant()?.isBefore(Instant.now().minusSeconds(300)) != true
}
