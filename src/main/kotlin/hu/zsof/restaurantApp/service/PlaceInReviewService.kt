package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.PlaceInReview
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.PlaceInReviewRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PlaceInReviewService(private val placeInReviewRepository: PlaceInReviewRepository, private val placeService: PlaceService) {

    fun newPlace(newPlace: PlaceInReview): PlaceInReview {
        val theNewPlace = PlaceInReview()
        theNewPlace.name = newPlace.name
        theNewPlace.address = newPlace.address
        theNewPlace.type = newPlace.type
        theNewPlace.price = newPlace.price
        theNewPlace.image = newPlace.image
        theNewPlace.filter = newPlace.filter
        theNewPlace.phoneNumber = newPlace.phoneNumber
        theNewPlace.email = newPlace.email
        theNewPlace.web = newPlace.web
        theNewPlace.latitude = newPlace.latitude
        theNewPlace.longitude = newPlace.longitude
        theNewPlace.openDetails = newPlace.openDetails
        theNewPlace.rate = 0.0f
        theNewPlace.usersNumber = 0
        theNewPlace.user = newPlace.user
        theNewPlace.problem = newPlace.problem
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

    fun addProblemToReview(placeId: Long, problem: String): PlaceInReviewDto {
        val placeInReview = getPlaceById(placeId)
        placeInReview.problem = problem
        return placeInReviewRepository.save(placeInReview).convertToDto()
    }


    fun acceptPlace(placeId: Long): PlaceDto {
        val placeInReview = getPlaceById(placeId)
        //Add to Place table
        val newPlace = placeService.newPlace(placeInReview.convertToPlace()).convertToDto()

        //Delete from PlaceInReview table
        deletePlaceById(placeId)
        return newPlace
    }
}
