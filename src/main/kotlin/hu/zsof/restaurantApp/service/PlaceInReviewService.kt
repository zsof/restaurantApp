package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.PlaceInReview
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.PlaceInReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PlaceInReviewService(private val placeInReviewRepository: PlaceInReviewRepository) {

    fun newPlace(newPlace: PlaceInReview): PlaceInReviewDto {
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
        return placeInReviewRepository.save(theNewPlace).convertToDto()
    }


    fun getAllPlace(): MutableList<PlaceInReview> = placeInReviewRepository.findAll()

    fun getPlaceById(id: Long) = placeInReviewRepository.findById(id)

    fun deletePlaceById(id: Long): Boolean {
        return if (placeInReviewRepository.existsById(id)) {
            placeInReviewRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun deleteAll() = placeInReviewRepository.deleteAll()

    fun updatePlaceWithProblem(placeInReviewDto: PlaceInReviewDto): Optional<PlaceInReviewDto> {
        val placeInReviewOptional = placeInReviewRepository.findById(placeInReviewDto.id)
        if (!placeInReviewOptional.isPresent) {
            return Optional.empty()
        }
        val updatePlaceInReview = placeInReviewOptional.get()
        updatePlaceInReview.problem = placeInReviewDto.problem ?: updatePlaceInReview.problem
        return Optional.of(placeInReviewRepository.save(updatePlaceInReview).convertToDto())
    }
}
