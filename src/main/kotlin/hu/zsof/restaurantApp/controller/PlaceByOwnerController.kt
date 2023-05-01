package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.security.SecurityService
import hu.zsof.restaurantApp.security.SecurityService.Companion.TOKEN_NAME
import hu.zsof.restaurantApp.service.PlaceInReviewService
import hu.zsof.restaurantApp.service.PlaceService
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
            @RequestHeader(TOKEN_NAME) token: String
    ): ResponseEntity<PlaceInReviewDto> {
        val verification = securityService.verifyToken(token)
        val newPlaceInReview = placeInReviewService.savePlaceInReview(placeInReview, verification.userId)
        return ResponseEntity(newPlaceInReview.convertToDto(), HttpStatus.CREATED)
    }

    // Delete own place from Place table
    @DeleteMapping("places/{id}")
    fun deletePlaceById(
            @PathVariable id: Long,
            @RequestHeader(TOKEN_NAME) token: String
    ): ResponseEntity<Response> {
        val verification = securityService.verifyToken(token)
        placeService.deletePlaceByIdByUser(id, verification.userId)
        return ResponseEntity(Response(true), HttpStatus.OK)
    }

    @GetMapping("places")
    fun getAllPlaceByOwner(@RequestHeader(TOKEN_NAME) token: String): ResponseEntity<List<PlaceDto>> {
        val verification = securityService.verifyToken(token)
        val places: MutableList<Place> = placeService.getAllPlaceByOwner(verification.userId)
        return ResponseEntity<List<PlaceDto>>(places.convertToDto(), HttpStatus.OK)
    }

    @GetMapping("places-in-review")
    fun getAllPlaceInReviewByOwner(@RequestHeader(TOKEN_NAME) token: String): ResponseEntity<List<PlaceInReviewDto>> {
        val verification = securityService.verifyToken(token)
        val placesInReview: MutableList<PlaceInReview> = placeInReviewService.getAllPlaceInReviewByOwner(verification.userId)
        return ResponseEntity<List<PlaceInReviewDto>>(placesInReview.convertToDto(), HttpStatus.OK)
    }

    //Update place -TODO
    /* @PostMapping("update")
     fun updatePlace(  //??
             @RequestBody place: Place,
             @RequestHeader(TOKEN_NAME) token: String
     ): ResponseEntity<PlaceInReviewDto> {
         val verification = securityService.verifyToken(token)
         return ResponseEntity(placeService.updatePlace(place, verification.userId), HttpStatus.OK)
     }*/
}