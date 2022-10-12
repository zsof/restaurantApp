package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserProfileDto
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class LoggedUserController(
    private val userService: UserService,
    private val placeService: PlaceService
) {
    @PutMapping("update")
    fun updateProfile(
        @RequestBody userProfile: UserProfileDto,
        @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<UserDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val updatedUser = userService.updateProfile(verification.userId, userProfile)
        if (!updatedUser.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(updatedUser.get(), HttpStatus.OK)
    }

    /* @GetMapping("add-favplace/{placeId}")
     fun addFavPlaceForUser(
         @PathVariable placeId: Long,
         @CookieValue(AuthUtils.COOKIE_NAME) token: String?
     ): ResponseEntity<UserDto> {
         val verification = AuthUtils.verifyToken(token)
         if (!verification.verified) {
             return ResponseEntity(HttpStatus.UNAUTHORIZED)
         }

         val placeAdded = userService.addFavPlace(verification.userId, Place(placeId))
         if (!placeAdded.isPresent) {
             return ResponseEntity(HttpStatus.BAD_REQUEST)
         }
         return ResponseEntity(placeAdded.get(), HttpStatus.OK)
     }*/

    /* @GetMapping("favplaces")
     fun getUserFavPlaces(@CookieValue(AuthUtils.COOKIE_NAME) token: String?) {
     }*/

    @PostMapping()
    fun newPlace(
        @RequestBody place: Place,
        @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<Unit> { // ResponseEntity<Place> ??
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val newPlace = placeService.newPlace(place)
        return ResponseEntity(newPlace, HttpStatus.CREATED)
    }
}
