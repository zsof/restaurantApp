package hu.zsof.restaurantApp.dto

class PlaceMapDto(
        val id: Long = 0,
        val name: String = "",
        val address: String? = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
)
