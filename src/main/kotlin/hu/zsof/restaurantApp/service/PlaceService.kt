package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.repository.PlaceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PlaceService @Autowired constructor( private val placeRepository: PlaceRepository){
    fun newPlace(newPlace: Place) : Place{
       return placeRepository.save(newPlace)

    }

    fun getAllPlace() : MutableList<Place>{
        return placeRepository.findAll()
    }

    fun getPlaceById(id :Int): Optional<Place> {
        return placeRepository.findById(id)
    }
}