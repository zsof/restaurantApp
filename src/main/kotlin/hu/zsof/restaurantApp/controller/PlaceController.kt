package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.FilterDto
import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceMapDto
import hu.zsof.restaurantApp.model.*
import hu.zsof.restaurantApp.model.enum.Price
import hu.zsof.restaurantApp.model.enum.Type
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
            @RequestParam("image") file: MultipartFile?,
            @RequestParam("name") name: String,
            @RequestParam("address") address: String,
            @RequestParam("rate") rate: Float?,
            @RequestParam("price") price: Price,
            @RequestParam("phoneNumber") phoneNumber: String?,
            @RequestParam("email") email: String?,
            @RequestParam("web") web: String?,
            @RequestParam("latitude") latitude: Double,
            @RequestParam("longitude") longitude: Double,
            @RequestParam("usersNumber") usersNumber: Int,
            @RequestParam("type") type: Type,
            @RequestParam("filter") filter: Filter,
            @RequestParam("openDetails") openDetails: OpenDetails,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<PlaceDto> {
        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        var imagePath = ""

        if (file != null) {
            val fileName = StringBuilder()
            val fileNameAndPath: Path = Paths.get("images/restaurants", file.originalFilename)
            fileName.append(file.originalFilename)
            Files.write(fileNameAndPath, file.bytes)
            imagePath = fileNameAndPath.toString()
        }

        val newPlace = placeService.newPlace(Place(
                image = imagePath,
                name = name,
                address = address,
                rate = rate,
                price = price,
                phoneNumber = phoneNumber,
                email = email,
                web = web,
                latitude = latitude,
                longitude = longitude,
                usersNumber = usersNumber,
                type = type,
                filter = filter,
                openDetails = openDetails
        ))
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
