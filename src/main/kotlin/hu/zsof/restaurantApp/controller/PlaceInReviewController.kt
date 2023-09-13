package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.service.PlaceInReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/places-review")
class PlaceInReviewController(private val placeInReviewService: PlaceInReviewService) {

    /**
     * Methods that only admin can use
     */

    // Get all places from PlaceInReview table
    @GetMapping
    fun getAllPlace(): ResponseEntity<List<PlaceInReviewDto>> {
        val placesInReview: MutableList<PlaceInReview> = placeInReviewService.getAllPlaceInReview()
        return ResponseEntity<List<PlaceInReviewDto>>(placesInReview.convertToDto(), HttpStatus.OK)
    }

    //Get the modified items from Place table
    @GetMapping("places/modified")
    fun getModifiedPlaces(): ResponseEntity<List<PlaceDto>> {
        val modifiedPlaces: MutableList<Place> = placeInReviewService.getModifiedPlaces()
        return ResponseEntity<List<PlaceDto>>(modifiedPlaces.convertToDto(), HttpStatus.OK)
    }

    @PostMapping("accept/{placeId}")
    fun acceptPlace(
            @PathVariable placeId: Long,
            @RequestParam isModifiedPlace: Boolean
    ): ResponseEntity<Response> {
        placeInReviewService.acceptPlace(placeId, isModifiedPlace)
        return ResponseEntity(Response(true), HttpStatus.CREATED)
    }


    //Send report back if there is any problem
    @PostMapping("report/{placeId}")
    fun reportProblemPlace(
            @RequestBody problem: String,
            @PathVariable placeId: Long,
            @RequestParam isModifiedPlace: Boolean
    ): ResponseEntity<Response> {
        placeInReviewService.addProblemToReview(placeId, problem, isModifiedPlace)
        return ResponseEntity(Response(true), HttpStatus.OK)
    }
}