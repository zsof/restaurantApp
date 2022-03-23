package hu.zsof.restaurantApp.model

import javax.persistence.*

@Embeddable
data class Filter(

    var freeParking: Boolean = false,
    var glutenFree: Boolean = false

)
