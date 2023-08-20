package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.model.OpenDetails
import hu.zsof.restaurantApp.model.enum.Price
import hu.zsof.restaurantApp.model.enum.Type

class PlaceDto(
        val id: Long = 0,
        val name: String = "",
        val address: String = "",
        val rate: Float? = 2.0f,
        val ratedUsersNum: Int? = 0,
        val price: Price = Price.LOW,
        val image: String? = null,
        val type: Type = Type.RESTAURANT,
        val filter: Filter = Filter(),
        val openDetails: OpenDetails = OpenDetails(),
        val phoneNumber: String? = "",
        var email: String? = "",
        var web: String? = "",
        var usersNumber: Int = 0,
        var creatorName: String = "",
        var creatorId: Long = 0,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var isModified: Boolean = false,
        var problem: String? = null,
)