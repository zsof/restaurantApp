package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.enum.UserType

class UserDto(
        val id: Long = 0,
        val name: String = "",
        val nickName: String? = null,
        val email: String = "",
        val image: String? = null,
        val userType: UserType = UserType.USER,
        val favPlaceIds: MutableList<Long> = mutableListOf()
)
