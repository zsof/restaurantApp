package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.PlaceInReview
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.PlaceInReviewRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceInReviewService(private val placeInReviewRepository: PlaceInReviewRepository, private val placeService: PlaceService) {

    fun savePlaceInReview(newPlace: PlaceInReview, userId: Long): PlaceInReview {
        val theNewPlace = PlaceInReview(name = newPlace.name,
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
                user = MyUser(userId),
                problem = null
        )
        return placeInReviewRepository.save(theNewPlace)
    }

    fun getAllPlace(): MutableList<PlaceInReview> = placeInReviewRepository.findAll()

    fun getPlaceById(id: Long): PlaceInReview {
        val placeInReview = placeInReviewRepository.findById(id)
        if (placeInReview.isPresent) {
            return placeInReview.get()
        } else {
            throw MyException("Place in review not found", HttpStatus.NOT_FOUND)
        }
    }

    fun deletePlaceById(placeInReviewId: Long) {
        if (placeInReviewRepository.existsById(placeInReviewId)) {
            placeInReviewRepository.deleteById(placeInReviewId)
        } else {
            throw MyException("Place in review not found", HttpStatus.NOT_FOUND)
        }
    }

    fun addProblemToReview(placeId: Long, problem: String): PlaceInReview {
        val placeInReview = getPlaceById(placeId)
        placeInReview.problem = problem
        return placeInReviewRepository.save(placeInReview)
    }


    fun acceptPlace(placeId: Long): Place {
        val placeInReview = getPlaceById(placeId)
        //Add to Place table
        val newPlace = placeService.savePlace(placeInReview.convertToPlace())

        //Delete from PlaceInReview table
        deletePlaceById(placeId)
        return newPlace
    }
}