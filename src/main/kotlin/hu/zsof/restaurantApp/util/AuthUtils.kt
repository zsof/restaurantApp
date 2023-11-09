package hu.zsof.restaurantApp.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import hu.zsof.restaurantApp.model.response.VerificationResponse
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

object AuthUtils {

    private const val SECRET_KEY = "ResTaUr4nt4pP"
    private const val JWT_TOKEN_VALIDATE_TIME = 1 * 60 * 60 * 1000 // 1 hour
    private val JWTVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build()
    private const val IS_ADMIN = "is_admin"
    const val COOKIE_NAME = "jwt_token"

    val passwordEncoder = BCryptPasswordEncoder()

    fun createToken(id: Long, isAdmin: Boolean = false): String {
        val audience = id.toString()

        return JWT.create()
            .withAudience(audience)
            .withClaim(IS_ADMIN, isAdmin)
            .withExpiresAt(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDATE_TIME))
            .sign(Algorithm.HMAC256(SECRET_KEY))
    }

    fun verifyToken(token: String?): VerificationResponse {
        if (token.isNullOrEmpty()) {
            return VerificationResponse(verified = false, errorMessage = "Empty Token")
        }
        return try {
            val jwt: DecodedJWT = JWTVerifier.verify(token)
            if (jwt.getClaim(IS_ADMIN).isMissing) {
                return VerificationResponse(verified = false, errorMessage = "IS_ADMIN claim missing")
            }
            val isAdmin: Boolean = jwt.getClaim(IS_ADMIN).asBoolean()
            val userId: Long = jwt.audience[0].toLong()
            VerificationResponse(verified = true, isAdmin = isAdmin, userId = userId)
        } catch (e: Exception) {
            // Invalid signature/claims
            VerificationResponse(verified = false, errorMessage = e.localizedMessage)
        }
    }

    fun comparePassword(password: String, encodedPassword: String): Boolean {
        return BCryptPasswordEncoder().matches(password, encodedPassword)
    }
}