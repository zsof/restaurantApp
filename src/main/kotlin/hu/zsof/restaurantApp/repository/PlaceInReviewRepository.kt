package hu.zsof.restaurantApp.repository

import hu.zsof.restaurantApp.model.PlaceInReview
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaceInReviewRepository : JpaRepository<PlaceInReview, Long>