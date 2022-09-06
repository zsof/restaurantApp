package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.repository.PlaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceService(private val placeRepository: PlaceRepository) {
    fun newPlace(newPlace: Place) = placeRepository.save(newPlace)

    //mutablelist-->list
    fun getAllPlace(): List<Place> = placeRepository.findAll()

    fun getPlaceById(id: Long) = placeRepository.findById(id)

    fun deleteById(id: Long): Boolean {
        return if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun deleteAll() = placeRepository.deleteAll()
}