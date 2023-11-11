package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter

class UserUpdateProfileDto(
    val name: String?,
    val image: String?,
    val filters: Filter,
)