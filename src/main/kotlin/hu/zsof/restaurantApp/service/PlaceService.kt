package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.PlaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceService(private val placeRepository: PlaceRepository) {
    fun newPlace(newPlace: Place): PlaceDto {
        val theNewPlace = Place()
        theNewPlace.name = newPlace.name
        theNewPlace.address = newPlace.address
        theNewPlace.type = newPlace.type
        theNewPlace.price = newPlace.price
        theNewPlace.image = newPlace.image
        theNewPlace.filter = newPlace.filter
        theNewPlace.phoneNumber = newPlace.phoneNumber
        return placeRepository.save(newPlace).convertToDto()
    }

    fun getAllPlace(): MutableList<Place> = placeRepository.findAll()

    fun getPlaceById(id: Long) = placeRepository.findById(id)

    fun deletePlaceById(id: Long): Boolean {
        return if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun deleteAll() = placeRepository.deleteAll()
}
