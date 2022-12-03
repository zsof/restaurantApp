package hu.zsof.restaurantApp.model.response

class VerificationResponse(
    val verified: Boolean = false,
    val isAdmin: Boolean = false,
    val errorMessage: String = "",
    val userId: Long = -1
)