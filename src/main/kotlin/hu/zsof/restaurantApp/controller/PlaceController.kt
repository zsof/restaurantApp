package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/places")
class PlaceController(private val placeService: PlaceService) {

    @GetMapping("/{id}")
    fun getPlaceById(
        @PathVariable id: Long,
        @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceDto?> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val place: Optional<Place> = placeService.getPlaceById(id)
        if (!place.isPresent) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(place.get().convertToDto(), HttpStatus.OK)
    }

    @GetMapping
    fun getAllPlace(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<List<PlaceDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val places: MutableList<Place> = placeService.getAllPlace()
        return ResponseEntity<List<PlaceDto>>(places.convertToDto(), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Long,
        @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        return if (placeService.deletePlaceById(id)) {
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping
    fun deleteAllPlace(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<HttpStatus> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified || !verification.isAdmin) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        placeService.deleteAll()
        return ResponseEntity(HttpStatus.OK)
    }
}
