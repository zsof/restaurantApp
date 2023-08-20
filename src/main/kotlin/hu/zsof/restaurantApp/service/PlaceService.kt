package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.FilterDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.enum.Type
import hu.zsof.restaurantApp.repository.PlaceInReviewRepository
import hu.zsof.restaurantApp.repository.PlaceRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceService(private val placeRepository: PlaceRepository, private val placeInReviewRepository: PlaceInReviewRepository) {
    fun savePlace(newPlace: Place): Place {
        val theNewPlace = Place(
                name = newPlace.name,
                address = newPlace.address,
                type = newPlace.type,
                price = newPlace.price,
                image = newPlace.image,
                filter = newPlace.filter,
                phoneNumber = newPlace.phoneNumber,
                email = newPlace.email,
                web = newPlace.web,
                latitude = newPlace.latitude,
                longitude = newPlace.longitude,
                openDetails = newPlace.openDetails,
                rate = newPlace.rate,
                usersNumber = 0,
                creator = newPlace.creator
        )
        return placeRepository.save(theNewPlace)
    }

    fun getAllPlace(): MutableList<Place> = placeRepository.findAll().filter { it.isVisible }.toMutableList()
    fun getAllPlaceByOwner(creatorId: Long): MutableList<Place> {
        val ownerPlaces = mutableListOf<Place>()
        placeRepository.findAll().forEach {
            if (it.creator.id == creatorId) {
                ownerPlaces.add(it)
            }
        }
        return ownerPlaces
    }

    fun getPlaceById(id: Long): Place {
        val place = placeRepository.findById(id)
        if (place.isPresent) {
            return place.get()
        } else {
            throw MyException("Place not found", HttpStatus.NOT_FOUND)
        }
    }

    fun deletePlaceByIdByUser(placeId: Long, creatorId: Long) {
        val place = getPlaceById(placeId)

        if (place.creator.id == creatorId) {
            deletePlaceById(placeId)
        } else {
            throw MyException("User has no permission to delete this place", HttpStatus.FORBIDDEN)
        }
    }


    fun deletePlaceById(id: Long) {
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id)
        } else {
            throw MyException("Place not found", HttpStatus.NOT_FOUND)
        }
    }

    fun filterPlaces(filterItems: FilterDto): MutableList<Place> {
        val getAllFilteredPlace = getAllPlace().filter { restaurantList ->
            filterItems.filter.convertToList().compare(restaurantList.filter.convertToList())
            // todo ide nem lehet ? -> filterItems.type== restaurantList.type
        }.toMutableList()

        return if (filterItems.type?.equals(Type.EMPTY) == true) {
            getAllFilteredPlace
        } else {
            getAllFilteredPlace.filter { restaurantList ->
                filterItems.type == restaurantList.type
            }.toMutableList()
        }
    }


    fun updatePlace(place: Place, creatorId: Long): Place {
        val placeOptional = placeRepository.findById(place.id)
        if (!placeOptional.isPresent) {
            throw MyException("Place not found", HttpStatus.NOT_FOUND)
        }
        val updatedPlace = placeOptional.get()
        if (updatedPlace.creator.id == creatorId) {

            // Update place table with changes
            updatedPlace.name = place.name
            updatedPlace.address = place.address
            updatedPlace.type = place.type
            updatedPlace.price = place.price
            updatedPlace.image = place.image ?: updatedPlace.image
            updatedPlace.filter = place.filter
            updatedPlace.phoneNumber = place.phoneNumber ?: updatedPlace.phoneNumber
            updatedPlace.email = place.email ?: updatedPlace.email
            updatedPlace.web = place.web ?: updatedPlace.web
            updatedPlace.latitude = place.latitude
            updatedPlace.longitude = place.longitude
            updatedPlace.openDetails = place.openDetails

            //These data can not change by update the place
            updatedPlace.rate = updatedPlace.rate
            updatedPlace.usersNumber = updatedPlace.usersNumber
            updatedPlace.creator = updatedPlace.creator

            updatedPlace.isModified = true
            updatedPlace.isVisible = updatedPlace.isVisible


            //Save place from
            return placeRepository.save(updatedPlace)

        } else {
            throw MyException("User has no permission to update this place", HttpStatus.BAD_REQUEST)
        }

    }
}