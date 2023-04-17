package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.model.enum.Price
import hu.zsof.restaurantApp.model.enum.Type

class FilterDto(
        var filter: Filter = Filter(),
        var type: Type = Type.RESTAURANT,
        val rate: Float? = 0.0f,
        val price: Price = Price.LOW
)