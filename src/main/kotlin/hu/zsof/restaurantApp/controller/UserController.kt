package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.security.SecurityService.Companion.TOKEN_NAME
import hu.zsof.restaurantApp.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OWNER') or hasRole('ROLE_USER')")
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PutMapping("profile")
    fun updateProfile(
            @RequestBody userUpdateProfileDto: UserUpdateProfileDto,
            @RequestHeader(TOKEN_NAME) token: String,
            authentication: Authentication
    ): ResponseEntity<UserDto> {
        return ResponseEntity(userService.updateProfile(
                authentication.name.toLong(),
                userUpdateProfileDto).convertToDto(),
                HttpStatus.OK
        )
    }

    @GetMapping("profile")
    fun getUserProfile(
            @RequestHeader(TOKEN_NAME) token: String,
            authentication: Authentication
    ): ResponseEntity<UserDto> {
        return ResponseEntity(userService.getUserById(authentication.name.toLong()).convertToDto(), HttpStatus.OK)
    }

    @PostMapping("fav-places/{placeId}")
    fun addFavPlaceForUser(
            @PathVariable placeId: Long,
            @RequestHeader(TOKEN_NAME) token: String,
            authentication: Authentication
    ): ResponseEntity<UserDto> {
        return ResponseEntity<UserDto>(userService.addFavPlace(authentication.name.toLong(), placeId).convertToDto(), HttpStatus.OK)
    }

    @GetMapping("fav-places")
    fun getUserFavPlaces(
            @RequestHeader(TOKEN_NAME) token: String,
            authentication: Authentication
    ): ResponseEntity<List<PlaceDto>> {
        return ResponseEntity<List<PlaceDto>>(userService.getFavPlaces(authentication.name.toLong()).convertToDto(), HttpStatus.OK)
    }
}