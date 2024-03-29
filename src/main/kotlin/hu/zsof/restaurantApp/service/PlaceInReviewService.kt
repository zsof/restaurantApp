package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.PlaceInReview
import hu.zsof.restaurantApp.repository.PlaceInReviewRepository
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.util.ResourceUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceInReviewService(
    private val placeInReviewRepository: PlaceInReviewRepository,
    private val placeService: PlaceService,
    private val placeRepository: PlaceRepository,
) {

    // When owner add and save a new place
    fun savePlaceInReview(newPlace: PlaceInReview, userId: Long): PlaceInReview {
        val theNewPlace = PlaceInReview(
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
            creator = MyUser(userId),
            problem = null,
        )
        return placeInReviewRepository.save(theNewPlace)
    }

    // For admin
    fun getAllPlaceInReview(): MutableList<PlaceInReview> = placeInReviewRepository.findAll()

    // From Place - get modified places by admin - admin has to accept/report, but it must have in the Place list, cannot delete from it
    fun getModifiedPlaces(): MutableList<Place> = placeRepository.findAll().filter { it.isModified }.toMutableList()

    // For owner - just the specific owner's places
    fun getAllPlaceInReviewByOwner(creatorId: Long): MutableList<PlaceInReview> {
        val ownerPlaces = mutableListOf<PlaceInReview>()
        placeInReviewRepository.findAll().forEach {
            if (it.creator.id == creatorId) {
                ownerPlaces.add(it)
            }
        }
        return ownerPlaces
    }

    fun getPlaceInReviewById(id: Long): PlaceInReview {
        val placeInReview = placeInReviewRepository.findById(id)
        if (placeInReview.isPresent) {
            return placeInReview.get()
        } else {
            throw MyException("Place in review not found", HttpStatus.NOT_FOUND)
        }
    }

    fun getModifiedPlaceById(id: Long): Place {
        val modifiedPlace = getModifiedPlaces().find { it.id == id }
        if (modifiedPlace != null) {
            return modifiedPlace
        } else {
            throw MyException("Modified Place not found", HttpStatus.NOT_FOUND)
        }
    }

    fun deletePlaceInReviewByIdByOwner(placeInReviewId: Long, creatorId: Long) {
        val placeInReview = getPlaceInReviewById(placeInReviewId)
        if (placeInReview.creator.id == creatorId) {
            ResourceUtil.deleteImage(placeInReview.image)
            deletePlaceInReviewById(placeInReviewId)
        } else {
            throw MyException("User has no permission to delete this place", HttpStatus.FORBIDDEN)
        }
    }

    fun deletePlaceInReviewById(id: Long) {
        if (placeInReviewRepository.existsById(id)) {
            val placeInReview = getPlaceInReviewById(id)
            ResourceUtil.deleteImage(placeInReview.image)
            placeInReviewRepository.deleteById(id)
        } else {
            throw MyException("Place in Review not found", HttpStatus.NOT_FOUND)
        }
    }

    fun addProblemToReview(placeId: Long, problem: String, isModifiedPlace: Boolean) {
        if (isModifiedPlace.not()) {
            val placeInReview = getPlaceInReviewById(placeId)
            placeInReview.problem = problem

            placeInReviewRepository.save(placeInReview)
        } else {
            val modifiedPlace = getModifiedPlaceById(placeId)
            modifiedPlace.problem = problem
            modifiedPlace.isVisible = false

            placeRepository.save(modifiedPlace)
        }
    }

    fun acceptPlace(placeId: Long, isModifiedPlace: Boolean = false) {
        if (isModifiedPlace.not()) {
            val placeInReview = getPlaceInReviewById(placeId)
            // Add to Place table
            placeService.savePlace(placeInReview.convertToPlace())

            // Delete from PlaceInReview table
            deletePlaceInReviewById(placeId)
        } else {
            // If the place was modified and accept
            val modifiedPlace = getModifiedPlaceById(placeId)
            modifiedPlace.isModified = false
            modifiedPlace.isVisible = true

            placeRepository.save(modifiedPlace)
        }
    }
}