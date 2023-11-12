package hu.zsof.restaurantApp.security

import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.model.request.LoginData
import hu.zsof.restaurantApp.model.response.LoggedUserResponse
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.security.SecurityService.Companion.TOKEN_NAME
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.ValidationUtils
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class SecurityController(private val userService: UserService, private val securityService: SecurityService) {

    @GetMapping
    fun authorize(
        @RequestHeader(TOKEN_NAME) token: String,
    ): ResponseEntity<LoggedUserResponse> {
        val verification = securityService.verifyToken(token)
        val user = userService.getUserById(verification.userId)
        return ResponseEntity(LoggedUserResponse(true, "", "", user.convertToDto()), HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(
        authentication: Authentication,
        response: HttpServletResponse,
    ): ResponseEntity<LoggedUserResponse> {
        val user = userService.getUserByEmail(email = authentication.name)

        val token = securityService.generateToken(user = user)
        response.addHeader(TOKEN_NAME, "Bearer $token")
        return ResponseEntity(LoggedUserResponse(true, "", "Belépés sikeres!", user.convertToDto()), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun register(
        @RequestBody loginData: LoginData,
        @RequestHeader isOwner: Boolean?,
    ): ResponseEntity<Response> {
        if (loginData.email.isEmpty() || loginData.password.isEmpty()) {
            throw MyException("Email cím vagy jelszó üres.", HttpStatus.BAD_REQUEST)
        }
        if (ValidationUtils.checkEmailValidation(loginData.email) && ValidationUtils.checkPasswordValidation(loginData.password)) {
            try {
                userService.createUser(
                    MyUser(
                        email = loginData.email,
                        password = ValidationUtils.passwordEncoder.encode(loginData.password),
                        name = loginData.name,
                    ),
                    isOwner ?: false,
                )
            } catch (e: DataIntegrityViolationException) {
                throw MyException("Ez az email cím már használatban van.", HttpStatus.BAD_REQUEST)
            }
            return ResponseEntity(Response(true, "Regisztráció sikeres! Kérlek aktiváld fiókodat.", ""), HttpStatus.CREATED)
        } else {
            throw MyException("Az email cím vagy jelszó helytelen.", HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("verify/{id}/{secret}")
    fun verifyEmail(
        @PathVariable id: Long,
        @PathVariable secret: String,
    ): String {
        userService.verifyEmail(id, secret)

        return " <h2 style='color:DodgerBlue\n;'>Sikeresen regisztráltad az email címed!</h2>" +
            "<a href=\"https://play.google.com/store/apps/details?id=hu.zsof.restaurantappjetpacknew\">Nyisd meg az appot.</a>"
    }
}