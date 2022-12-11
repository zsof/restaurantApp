package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.FilterDto
import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceMapDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.model.enum.Price
import hu.zsof.restaurantApp.model.enum.Type
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
        if (!verification.verified) {
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
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val places: MutableList<Place> = placeService.getAllPlace()
        return ResponseEntity<List<PlaceDto>>(places.convertToDto(), HttpStatus.OK)
    }

    @GetMapping("map")
    fun getAllPlacesInMap(@CookieValue(AuthUtils.COOKIE_NAME) token: String?): ResponseEntity<List<PlaceMapDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val places: MutableList<Place> = placeService.getAllPlace()
        return ResponseEntity<List<PlaceMapDto>>(places.convertToPlaceMapDto(), HttpStatus.OK)
    }

    @PostMapping("new-place")
    fun newPlace(
            @RequestBody place: Place,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceDto> { //PlaceDto
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val newPlace = placeService.newPlace(place)

        return ResponseEntity(newPlace, HttpStatus.CREATED)
    }

    @PostMapping("filter")
    fun filterPlace(
            @RequestBody filters: FilterDto,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<List<PlaceDto>> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val filteredPlaces: MutableList<Place> = placeService.filterPlaces(filters)
        return ResponseEntity(filteredPlaces.convertToDto(), HttpStatus.OK)
    }
}
