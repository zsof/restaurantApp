package hu.zsof.restaurantApp.model.response

import hu.zsof.restaurantApp.dto.UserDto
class LoggedUserResponse(
        val isSuccess: Boolean = false,
        val successMessage: String = "",
        val error: String = "",
        val user: UserDto? = null
)