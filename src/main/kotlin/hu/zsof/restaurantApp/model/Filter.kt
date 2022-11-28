package hu.zsof.restaurantApp.model

import javax.persistence.*

@Embeddable
class Filter(

        var glutenFree: Boolean = false,
        var lactoseFree: Boolean = false,
        var vegetarian: Boolean = false,
        var vegan: Boolean = false,
        var fastFood: Boolean = false,

        var parkingAvailable: Boolean = false,
        var dogFriendly: Boolean = false,
        var familyPlace: Boolean = false,
        var delivery: Boolean = false,
        var creditCard: Boolean = false,
)
