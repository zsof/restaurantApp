package hu.zsof.restaurantApp.dto

class CommentDto(
    val id: Long = -1,
    var message: String = "",
    var userId: Long = -1,
    var placeId: Long = -1,
    val userName: String,
)