package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.model.extension.Response
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/loggeduser")  //Todo lehetne valami loggedUser
class LoggedUserController(
        private val userService: UserService,
        private val placeService: PlaceService
) {
    /**
     * These functions are available for logged users and admins
     */

    @PutMapping("update-profile")
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

    @GetMapping("get-profile")
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

    @PostMapping("add-favplace/{placeId}")
    fun addFavPlaceForUser(
            @PathVariable placeId: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val modifiedPlace = userService.addFavPlace(verification.userId, placeId)
        if (!modifiedPlace.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity<PlaceDto>(modifiedPlace.get(), HttpStatus.OK)
    }

    @GetMapping("favplaces")
    fun getUserFavPlaces(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<List<PlaceDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val favPlaces = userService.getFavPlaces(verification.userId)
        return ResponseEntity<List<PlaceDto>>(favPlaces.get(), HttpStatus.OK)
    }

    @PostMapping("new-place")
    fun newPlace(
            @RequestBody place: Place,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val newPlace = placeService.newPlace(place)
        return ResponseEntity(newPlace, HttpStatus.CREATED)
    }
}
