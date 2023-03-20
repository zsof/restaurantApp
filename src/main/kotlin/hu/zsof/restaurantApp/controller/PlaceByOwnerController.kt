package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.security.SecurityService
import hu.zsof.restaurantApp.security.SecurityService.Companion.TOKEN_NAME
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
class PlaceByOwnerController(private val placeService: PlaceService, private val placeInReviewService: PlaceInReviewService, private val securityService: SecurityService) {

    /**
     * Methods that only owner and admin can use
     */

    // Add new place - it will be stored in PlaceInReview table until admin checks it
    @PostMapping("new-place")
    fun newPlace(
            @RequestBody placeInReview: PlaceInReview,
    ): ResponseEntity<PlaceInReviewDto> {
        val newPlaceInReview = placeInReviewService.newPlace(placeInReview)
        return ResponseEntity(newPlaceInReview.convertToDto(), HttpStatus.CREATED)
    }

    // Delete own place from Place table
    @DeleteMapping("places/{id}")
    fun deletePlaceById(
            @PathVariable id: Long,
            @CookieValue(TOKEN_NAME) token: String
    ): ResponseEntity<Response> {
        val verification = securityService.verifyToken(token)
        placeService.deletePlaceByIdByUser(id, verification.userId)
        return ResponseEntity(Response(true), HttpStatus.OK)
    }

    //Update place -TODO
   /* @PostMapping("update")
    fun updatePlace(  //??
            @RequestBody place: Place,
            @CookieValue(TOKEN_NAME) token: String
    ): ResponseEntity<PlaceInReviewDto> {
        val verification = securityService.verifyToken(token)
        return ResponseEntity(placeService.updatePlace(place, verification.userId), HttpStatus.OK)
    }*/
}
