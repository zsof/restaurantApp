package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.model.Place
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/places")
class PlaceController @Autowired constructor(private val placeService: PlaceService) {

    @PostMapping()
    fun newPlace(@RequestBody place: Place): ResponseEntity<Place> {
        val newPlace = placeService.newPlace(place)
        return ResponseEntity(newPlace, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getPlaceById(@PathVariable id: Int): ResponseEntity<Place?> {
        val place: Optional<Place> = placeService.getPlaceById(id)
        if (!place.isPresent)
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        return ResponseEntity(place.get(), HttpStatus.OK)
    }

    @GetMapping
    fun getAllPlace(): ResponseEntity<List<Place>> {
        val places: List<Place> = placeService.getAllPlace()
        return ResponseEntity<List<Place>>(places, HttpStatus.OK)
    }
}