package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter

class UserUpdateProfileDto(
    val name: String?,
    val nickName: String?,
    val password: String?,
    val image: String?,
    val email: String?,
    val filters: Filter
)