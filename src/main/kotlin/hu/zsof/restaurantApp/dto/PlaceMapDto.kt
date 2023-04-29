package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter

class PlaceMapDto(
        val id: Long = 0,
        val name: String = "",
        val address: String? = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var filterItems: Filter = Filter()
)