package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.security.SecurityService
import hu.zsof.restaurantApp.security.SecurityService.Companion.TOKEN_NAME
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OWNER') or hasRole('ROLE_USER')")
@RequestMapping("/users")
class UserController(private val userService: UserService, private val securityService: SecurityService) {

    @PutMapping("profile")
    fun updateProfile(
            @RequestBody userUpdateProfileDto: UserUpdateProfileDto,
            @CookieValue(TOKEN_NAME) token: String
    ): ResponseEntity<UserDto> {
        val verification = securityService.verifyToken(token)
        return ResponseEntity(userService.updateProfile(verification.userId, userUpdateProfileDto), HttpStatus.OK)
    }

    @GetMapping("profile")
    fun getUserProfile(
            @CookieValue(TOKEN_NAME) token: String
    ): ResponseEntity<UserDto> {
        val verification = securityService.verifyToken(token)
        return ResponseEntity(userService.getUserById(verification.userId).convertToDto(), HttpStatus.OK)
    }

    @PostMapping("fav-places/{placeId}")
    fun addFavPlaceForUser( //??
            @PathVariable placeId: Long,
            @CookieValue(TOKEN_NAME) token: String
    ): ResponseEntity<UserDto> {
        val verification = securityService.verifyToken(token)
        return ResponseEntity<UserDto>(userService.addFavPlace(verification.userId, placeId), HttpStatus.OK)
    }

    @GetMapping("fav-places")
    fun getUserFavPlaces(@CookieValue(TOKEN_NAME) token: String): ResponseEntity<List<PlaceDto>> {
        val verification = securityService.verifyToken(token)
        return ResponseEntity<List<PlaceDto>>(userService.getFavPlaces(verification.userId), HttpStatus.OK)
    }
}
