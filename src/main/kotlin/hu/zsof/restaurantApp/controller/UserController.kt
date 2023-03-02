package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
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
class UserController(private val userService: UserService) {

    @PutMapping("profile")
    fun updateProfile(
            @RequestBody userUpdateProfileDto: UserUpdateProfileDto,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<UserDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val updatedUser = userService.updateProfile(verification.userId, userUpdateProfileDto)
        if (!updatedUser.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(updatedUser.get(), HttpStatus.OK)
    }

    @GetMapping("profile")
    fun getUserProfile(
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<UserDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val getUser = userService.getUserById(verification.userId)
        if (!getUser.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(getUser.get().convertToDto(), HttpStatus.OK)
    }

    @PostMapping("fav-places/{placeId}")
    fun addFavPlaceForUser(
            @PathVariable placeId: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<UserDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val modifiedUser = userService.addFavPlace(verification.userId, placeId)
        if (!modifiedUser.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity<UserDto>(modifiedUser.get(), HttpStatus.OK)
    }

    @GetMapping("fav-places")
    fun getUserFavPlaces(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<List<PlaceDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val favPlaces = userService.getFavPlaces(verification.userId)
        return ResponseEntity<List<PlaceDto>>(favPlaces.get(), HttpStatus.OK)
    }
}
