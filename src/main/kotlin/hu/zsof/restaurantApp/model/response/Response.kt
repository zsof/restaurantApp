package hu.zsof.restaurantApp.model.response

class Response(
    val isSuccess: Boolean = false,
    val successMessage: String = "",
    val error: String = "",
)