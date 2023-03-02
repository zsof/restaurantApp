package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
class AdminController(private val placeService: PlaceService, private val userService: UserService) {

    /**
     * User controller functions
     */
    @GetMapping("users/{id}")
    fun getUserById(
            @PathVariable id: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<UserDto?> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val user: Optional<MyUser> = userService.getUserById(id)
        if (!user.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(user.get().convertToDto(), HttpStatus.OK)
    }

    @DeleteMapping("users/{id}")
    fun deleteUserById(
            @PathVariable id: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        return try {
            userService.deleteUserById(id)
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/users")
    fun getAllUser(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<List<UserDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val users: MutableList<MyUser> = userService.getAllUser()
        return ResponseEntity<List<UserDto>>(users.convertToDto(), HttpStatus.OK)
    }

    @DeleteMapping("/users")
    fun deleteAllUser(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        return try {
            userService.deleteAllUser()
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}