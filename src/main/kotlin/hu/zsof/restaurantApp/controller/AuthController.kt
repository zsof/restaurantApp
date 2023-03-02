/*
package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.model.response.LoggedUserResponse
import hu.zsof.restaurantApp.model.request.LoginData
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import hu.zsof.restaurantApp.util.ValidationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(private val userService: UserService) {

    @PostMapping("/login")
    fun login(@RequestBody loginData: LoginData, response: HttpServletResponse): ResponseEntity<LoggedUserResponse> {
        if (loginData.email.isEmpty() || loginData.password.isEmpty()) {
            return ResponseEntity(LoggedUserResponse(isSuccess = false, "", "Email or password is empty"), HttpStatus.BAD_REQUEST)
        }
        val getUser = userService.getUserByEmail(email = loginData.email)
        if (getUser.isPresent) {
            val user = getUser.get()
            if (AuthUtils.comparePassword(loginData.password, user.password)) {
                val token = AuthUtils.createToken(user.id, user.isAdmin)
                val cookie = Cookie(AuthUtils.COOKIE_NAME, token)
                response.addCookie(cookie)
                return ResponseEntity(LoggedUserResponse(isSuccess = true, successMessage = "Login is successful", error = "", user.convertToDto()), HttpStatus.OK)
            }
            return ResponseEntity(LoggedUserResponse(false, "", "Email or password is wrong"), HttpStatus.UNAUTHORIZED)
        } else {
            return ResponseEntity(LoggedUserResponse(false, "", "User not found"), HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody loginData: LoginData): ResponseEntity<Response> {
        if (loginData.email.isEmpty() || loginData.password.isEmpty() || loginData.name.isEmpty()) {
            return ResponseEntity(Response(false, "", "Email, password or name is empty"), HttpStatus.BAD_REQUEST)
        }
        if (ValidationUtils.checkEmailValidation(loginData.email) && ValidationUtils.checkPasswordValidation(loginData.password)) {
            try {
                userService.createUser(MyUser(email = loginData.email, password = AuthUtils.passwordEncoder.encode(loginData.password), name = loginData.name, nickName = loginData.nickName))
            } catch (e: DataIntegrityViolationException) {
                return ResponseEntity(Response(false, "", "Email is already in use"), HttpStatus.BAD_REQUEST)
            }
            return ResponseEntity(Response(true, "Register is successful", ""), HttpStatus.CREATED)
        } else {
            return ResponseEntity(Response(false, "", "Email or password is invalid"), HttpStatus.BAD_REQUEST)
        }
    }
}
*/
