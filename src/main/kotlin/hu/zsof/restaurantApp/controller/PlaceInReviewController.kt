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
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/places-review")
class PlaceInReviewController(private val placeInReviewService: PlaceInReviewService, private val placeService: PlaceService) {

    /**
     * Methods that only admin can use
     */

    // Get place from PlaceInReview table
    @GetMapping("/{id}")
    fun getPlaceById(
            @PathVariable id: Long
    ): ResponseEntity<PlaceInReviewDto?> {
        return ResponseEntity(placeInReviewService.getPlaceInReviewById(id).convertToDto(), HttpStatus.OK)
    }

    // Get all places from PlaceInReview table
    @GetMapping
    fun getAllPlace(): ResponseEntity<List<PlaceInReviewDto>> {
        val placesInReview: MutableList<PlaceInReview> = placeInReviewService.getAllPlaceInReview()
        return ResponseEntity<List<PlaceInReviewDto>>(placesInReview.convertToDto(), HttpStatus.OK)
    }

    // Delete any place from Place table
    @DeleteMapping("places/{id}")
    fun deletePlaceById(
            @PathVariable id: Long,
    ): ResponseEntity<Response> {
        placeInReviewService.deletePlaceInReviewById(id)
        return ResponseEntity(Response(true), HttpStatus.OK)
    }

    @PostMapping("accept/{placeId}")
    fun acceptPlace(
            @PathVariable placeId: Long,
    ): ResponseEntity<PlaceDto> {
        return ResponseEntity(placeInReviewService.acceptPlace(placeId).convertToDto(), HttpStatus.CREATED)
    }


    //Send report back if there is any problem
    @PostMapping("report/{placeId}")
    fun reportProblemPlace(
            @RequestBody problem: String,
            @PathVariable placeId: Long,
    ): ResponseEntity<PlaceInReviewDto> {
        return ResponseEntity(placeInReviewService.addProblemToReview(placeId, problem).convertToDto(), HttpStatus.OK)
    }
}