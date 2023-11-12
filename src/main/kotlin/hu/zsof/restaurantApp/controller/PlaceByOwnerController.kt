package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.service.PlaceInReviewService
import hu.zsof.restaurantApp.service.PlaceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OWNER')")
@RequestMapping("/places-owner")
class PlaceByOwnerController(
    private val placeService: PlaceService,
    private val placeInReviewService: PlaceInReviewService,
) {

    /**
     * Methods that only owner and admin can use
     */

    // Add new place - it will be stored in PlaceInReview table until admin checks it
    @PostMapping("new-place")
    fun newPlace(
        @RequestBody placeInReview: PlaceInReview,
        authentication: Authentication,
    ): ResponseEntity<PlaceInReviewDto> {
        val newPlaceInReview = placeInReviewService.savePlaceInReview(placeInReview, authentication.name.toLong())
        return ResponseEntity(newPlaceInReview.convertToDto(), HttpStatus.CREATED)
    }

    // Delete own place from Place table
    @DeleteMapping("places/{id}")
    fun deletePlaceById(
        @PathVariable id: Long,
        authentication: Authentication,
    ): ResponseEntity<Response> {
        placeService.deletePlaceByIdByUser(id, authentication.name.toLong())
        return ResponseEntity(Response(true), HttpStatus.OK)
    }

    // Delete any place from PlaceIntReview table
    @DeleteMapping("place-review/{id}")
    fun deletePlaceInReviewById(
        @PathVariable id: Long,
        authentication: Authentication,
    ): ResponseEntity<Response> {
        placeInReviewService.deletePlaceInReviewByIdByUser(id, authentication.name.toLong())
        return ResponseEntity(Response(true), HttpStatus.OK)
    }

    @GetMapping("places")
    fun getAllPlaceByOwner(
        authentication: Authentication,
    ): ResponseEntity<List<PlaceDto>> {
        val places: MutableList<Place> = placeService.getAllPlaceByOwner(authentication.name.toLong())
        return ResponseEntity<List<PlaceDto>>(places.convertToDto(), HttpStatus.OK)
    }

    @GetMapping("places-in-review")
    fun getAllPlaceInReviewByOwner(
        authentication: Authentication,
    ): ResponseEntity<List<PlaceInReviewDto>> {
        val placesInReview: MutableList<PlaceInReview> = placeInReviewService.getAllPlaceInReviewByOwner(authentication.name.toLong())
        return ResponseEntity<List<PlaceInReviewDto>>(placesInReview.convertToDto(), HttpStatus.OK)
    }

    // Get place from PlaceInReview table
    @GetMapping("/{id}")
    fun getPlaceById(
        @PathVariable id: Long,
    ): ResponseEntity<PlaceInReviewDto?> {
        return ResponseEntity(placeInReviewService.getPlaceInReviewById(id).convertToDto(), HttpStatus.OK)
    }

    @PostMapping("update")
    fun updatePlace(
        @RequestBody place: Place,
        authentication: Authentication,
    ): ResponseEntity<PlaceDto> {
        val modifiedPlace = placeService.updatePlace(place, authentication.name.toLong()).convertToDto()
        return ResponseEntity(modifiedPlace, HttpStatus.OK)
    }
}