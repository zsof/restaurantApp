package hu.zsof.restaurantApp.dto

class UserDto(
    val id: Long = 0,
    val name: String = "",
    val nickName: String? = null,
    val email: String = "",
    val admin: Boolean = false,
    val favPlaces: MutableList<PlaceDto> = mutableListOf()
)
