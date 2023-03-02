package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.service.PlaceInReviewService
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/places-review")
class PlaceInReviewController(private val placeInReviewService: PlaceInReviewService, private val placeService: PlaceService) {

    /**
     * Methods that only admin can use
     */

    // Get place from PlaceInReview table
    @GetMapping("/{id}")
    fun getPlaceById(
            @PathVariable id: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceInReviewDto?> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val placeInReview: Optional<PlaceInReview> = placeInReviewService.getPlaceById(id)
        if (!placeInReview.isPresent) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(placeInReview.get().convertToDto(), HttpStatus.OK)
    }

    // Get all places from PlaceInReview table
    @GetMapping
    fun getAllPlace(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<List<PlaceInReviewDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val placesInReview: MutableList<PlaceInReview> = placeInReviewService.getAllPlace()
        return ResponseEntity<List<PlaceInReviewDto>>(placesInReview.convertToDto(), HttpStatus.OK)
    }

    // Delete any place from Place table
    @DeleteMapping("places/{id}")
    fun deletePlaceById(
            @PathVariable id: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        return if (placeService.deletePlaceById(id)) {
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    // Delete all places from Place table
    @DeleteMapping("places")
    fun deleteAllPlace(
            @PathVariable id: Long,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        return try {
            placeService.deleteAll()
            ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("accept")
    fun acceptPlace(
            @RequestBody place: Place,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        //Add to Place table
        val newPlace = placeService.newPlace(place)

        //Delete from PlaceInReview table
        if (placeInReviewService.deletePlaceById(place.id)) {
            ResponseEntity(null, HttpStatus.OK)
            //TODO így is jön vissza responsenetity, hogy nincs előtte a return?
        } else ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity(newPlace, HttpStatus.CREATED)
    }


    //Send report back if there is any problem
    @PostMapping
    fun reportProblemPlace(
            @RequestBody placeInReviewDto: PlaceInReviewDto,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceInReviewDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val updatedPlaceInReview = placeInReviewService.updatePlaceWithProblem(placeInReviewDto)
        if (!updatedPlaceInReview.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(updatedPlaceInReview.get(), HttpStatus.OK)
    }
}

