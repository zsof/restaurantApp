package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.model.enum.Category
import hu.zsof.restaurantApp.model.enum.Price

class PlaceDto(
    val id: Long = 0,
    val name: String = "",
    val address: String? = "",
    val rate: Float? = 2.0f,
    val price: Price = Price.LOW,
    val image: String? = null,
    val category: Category = Category.RESTAURANT,
    val filter: Filter = Filter()
)
