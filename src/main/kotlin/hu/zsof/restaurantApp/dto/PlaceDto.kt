package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.model.enum.Category

class PlaceDto(
    val id: Long = 0,
    val name: String = "",
    val address: String? = "",
    val rate: Float? = 2.0f,
    val price: Float? = 2.0f,
    val image: String? = null,
    val category: Category = Category.RESTAURANT,
    val filter: Filter = Filter()
)
