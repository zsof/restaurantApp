package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.FilterDto
import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceMapDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_OWNER') or hasRole('ROLE_USER')")
@RequestMapping("/places")
class PlaceByUserController(private val placeService: PlaceService) {

    /**
     * Methods that all user can use
     */

    @GetMapping("/{id}")
    fun getPlaceById(@PathVariable id: Long): ResponseEntity<PlaceDto?> {
        return ResponseEntity(placeService.getPlaceById(id).convertToDto(), HttpStatus.OK)
    }

    @GetMapping
    fun getAllPlace(): ResponseEntity<List<PlaceDto>> {
        val places: MutableList<Place> = placeService.getAllPlace()
        return ResponseEntity<List<PlaceDto>>(places.convertToDto(), HttpStatus.OK)
    }

    @GetMapping("map")
    fun getAllPlacesInMap(): ResponseEntity<List<PlaceMapDto>> {
        val places: MutableList<Place> = placeService.getAllPlace()
        return ResponseEntity<List<PlaceMapDto>>(places.convertToPlaceMapDto(), HttpStatus.OK)
    }

    @PostMapping("filter")
    fun filterPlace(
            @RequestBody filters: FilterDto,
    ): ResponseEntity<List<PlaceDto>> {
        val filteredPlaces: MutableList<Place> = placeService.filterPlaces(filters)
        return ResponseEntity(filteredPlaces.convertToDto(), HttpStatus.OK)
    }
}
