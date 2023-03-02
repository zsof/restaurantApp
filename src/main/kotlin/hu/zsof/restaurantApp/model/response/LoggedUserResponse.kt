package hu.zsof.restaurantApp.model.response

import hu.zsof.restaurantApp.dto.UserDto
class LoggedUserResponse(
        val isSuccess: Boolean = false,
        val error: String = "",
        val successMessage: String = "",
        val user: UserDto? = null
)