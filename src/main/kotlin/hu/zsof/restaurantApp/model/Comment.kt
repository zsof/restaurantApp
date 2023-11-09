package hu.zsof.restaurantApp.model

import hu.zsof.restaurantApp.dto.CommentDto
import hu.zsof.restaurantApp.service.UserService
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,
        var message: String = "",
        var userId: Long = -1,
        var placeId: Long = -1
)

fun Comment.convertToDto(userService: UserService): CommentDto {
    return CommentDto(
            id = this.id,
            message = message,
            userId = userId,
            placeId = placeId,
            userName = userService.getUserNameById(userId)
    )
}

fun List<Comment>.convertToDto(userService: UserService): MutableList<CommentDto> {
    val commentDtos = mutableListOf<CommentDto>()
    this.forEach {
        commentDtos.add(it.convertToDto(userService))
    }
    return commentDtos
}