package hu.zsof.restaurantApp.security

import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.response.VerificationResponse
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class SecurityService(private val jwtEncoder: JwtEncoder, private val jwtDecoder: JwtDecoder) {

    companion object {
        const val CLAIM_ROLE = "role"
        const val CLAIM_USERNAME = "username"
        const val ROLE_ADMIN = "ROLE_ADMIN"
        const val ROLE_OWNER = "ROLE_OWNER"
        const val ROLE_USER = "ROLE_USER"
        const val ISSUER = "restaurant_v1"
        const val JWT_TOKEN_VALIDITY = 1 * 60 * 60 * 1000 // 1 hour
        const val TOKEN_NAME = "Authorization"
    }

    fun generateToken(user: MyUser): String {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(JWT_TOKEN_VALIDITY.toLong()))
                .subject(user.id.toString())
                .claim(CLAIM_USERNAME, user.email)
                .claim(CLAIM_ROLE, user.userType)
                .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }

    fun verifyToken(bearerToken: String): VerificationResponse {
        if (bearerToken.isNullOrEmpty()) {
            throw MyException("Jwt token empty", HttpStatus.UNAUTHORIZED)
        }
        val token = bearerToken.replace("Bearer ", "")
        val jwt: Jwt = jwtDecoder.decode(token)
        val role: String = jwt.getClaim(CLAIM_ROLE)
        val isAdmin = role == ROLE_ADMIN
        val userId: Long = jwt.subject.toLong()
        if (jwt.expiresAt == null || jwt.expiresAt?.isBefore(Instant.now()) == true) {
            throw MyException("Jwt Expired", HttpStatus.UNAUTHORIZED)
        }
        return VerificationResponse(verified = true, isAdmin = isAdmin, userId = userId)
    }
}