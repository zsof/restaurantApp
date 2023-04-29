package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.security.SecurityService.Companion.ROLE_USER

class UserDto(
        val id: Long = 0,
        val name: String = "",
        val nickName: String? = null,
        val email: String = "",
        val image: String? = null,
        val userType: String = ROLE_USER,
        val favPlaceIds: MutableList<Long> = mutableListOf(),
        val filterItems: Filter = Filter()
)