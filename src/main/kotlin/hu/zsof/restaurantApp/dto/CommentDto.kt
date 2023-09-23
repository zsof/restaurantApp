package hu.zsof.restaurantApp.dto

import java.time.Instant

class CommentDto(
        val id: Long = -1,
        var message: String = "",
        var userId: Long = -1,
        var createDate: Instant,
        var placeId: Long = -1,
        val userName: String
)