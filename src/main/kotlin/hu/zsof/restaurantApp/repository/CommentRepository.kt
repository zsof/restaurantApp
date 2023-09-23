package hu.zsof.restaurantApp.repository

import hu.zsof.restaurantApp.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    fun findAllByPlaceId(placeId: Long): List<Comment>
    fun findAllByUserId(userId: Long): List<Comment>
}