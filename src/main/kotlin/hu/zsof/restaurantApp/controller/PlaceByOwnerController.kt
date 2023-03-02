package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.service.PlaceInReviewService
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OWNER')")
@RequestMapping("/places-owner")
class PlaceByOwnerController(private val placeService: PlaceService, private val placeInReviewService: PlaceInReviewService) {

    /**
     * Methods that only owner and admin can use
     */

    // Add new place - it will be stored in PlaceInReview table until admin checks it
    @PostMapping("new-place")
    fun newPlace(
            @RequestBody placeInReview: PlaceInReview,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceInReviewDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val newPlaceInReview = placeInReviewService.newPlace(placeInReview)

        return ResponseEntity(newPlaceInReview, HttpStatus.CREATED)
    }

    // Delete own place from Place table
    @DeleteMapping("places/{id}")
    fun deletePlaceById(
            @PathVariable id: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        return if (placeService.deletePlaceByIdByUser(id, verification.userId)) {
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    //Update place
    @PostMapping  //???
    fun updatePlace(
            @RequestBody place: Place,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceInReviewDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val updatedPlaceInReview = placeService.updatePlace(place)
        if (!updatedPlaceInReview.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(updatedPlaceInReview.get(), HttpStatus.OK)
    }

}
