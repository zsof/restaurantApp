package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.FilterDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.PlaceInReview
import hu.zsof.restaurantApp.model.convertToDto
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
                user = newPlace.user
        )
        return placeRepository.save(theNewPlace)
    }

    fun getAllPlace(): MutableList<Place> = placeRepository.findAll()

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

        if (place.user.id == creatorId) {
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

        return getAllFilteredPlace.filter { restaurantList ->
            filterItems.type == restaurantList.type
        }.toMutableList()
    }


    fun updatePlace(place: Place, creatorId: Long): PlaceInReviewDto {
        // todo modosítás oldal ahol jová lehet hagyni modosításokat - eredeti és módosított dolgok egymás mellett látszanak, nem kerül ki rendes oldalra a módosítás amig nem fogadta admin el --> el kell addig tárolni
        if (place.user.id == creatorId) {

            // Add place to PlaceInReview table with changes
            val newPlaceInReviewWithUpdates = PlaceInReview()
            newPlaceInReviewWithUpdates.name = place.name
            newPlaceInReviewWithUpdates.address = place.address
            newPlaceInReviewWithUpdates.type = place.type
            newPlaceInReviewWithUpdates.price = place.price
            newPlaceInReviewWithUpdates.image = place.image
            newPlaceInReviewWithUpdates.filter = place.filter
            newPlaceInReviewWithUpdates.phoneNumber = place.phoneNumber
            newPlaceInReviewWithUpdates.email = place.email
            newPlaceInReviewWithUpdates.web = place.web
            newPlaceInReviewWithUpdates.latitude = place.latitude
            newPlaceInReviewWithUpdates.longitude = place.longitude
            newPlaceInReviewWithUpdates.openDetails = place.openDetails
            newPlaceInReviewWithUpdates.rate = 0.0f
            newPlaceInReviewWithUpdates.usersNumber = 0
            newPlaceInReviewWithUpdates.user = place.user

            //Save place to PlaceInReview table
            val placeInReviewWithUpdates = placeInReviewRepository.save(newPlaceInReviewWithUpdates).convertToDto()

            //Delete place from Place table
            // todo id generált, megegyik a placein review idja a place rendes idjával? valószínuleg nem
            placeRepository.deleteById(place.id)

            // Add modified place to PlaceInReview able
            return placeInReviewWithUpdates
        } else {
            throw MyException("User has no permission to update this place", HttpStatus.FORBIDDEN)
        }

    }
}