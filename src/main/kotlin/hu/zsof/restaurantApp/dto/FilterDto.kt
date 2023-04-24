package hu.zsof.restaurantApp.dto

import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.model.enum.Price
import hu.zsof.restaurantApp.model.enum.Type

class FilterDto(
        var filter: Filter = Filter(),
        var type: Type? = null,
        val rate: Float? = null,
        val price: Price? = null
)