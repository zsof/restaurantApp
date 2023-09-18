package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.Comment
import hu.zsof.restaurantApp.model.request.CommentData
import hu.zsof.restaurantApp.repository.CommentRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class CommentService(
        private val commentRepository: CommentRepository,
        private val placeService: PlaceService,
        private val userService: UserService
) {
    fun addComment(comment: CommentData, userId: Long): Comment {
        placeService.getPlaceById(comment.placeId)
        val newComment = Comment(
                message = comment.message,
                createDate = Instant.now(),
                placeId = comment.placeId,
                userId = userId,
                userName = userService.getUserById(userId).name
        )
        return commentRepository.save(newComment)
    }

    fun getCommentsByPlaceId(placeId: Long): List<Comment> {
        return commentRepository.findAllByPlaceId(placeId)
    }

    fun deleteCommentById(id: Long) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id)
        } else {
            throw MyException("Comment not found", HttpStatus.NOT_FOUND)
        }
    }
}