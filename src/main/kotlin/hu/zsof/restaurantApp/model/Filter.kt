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
) {
    fun convertToList(): FilterList {
        return FilterList(
                mutableListOf(
                        glutenFree,
                        lactoseFree,
                        vegetarian,
                        vegan,
                        fastFood,
                        parkingAvailable,
                        dogFriendly,
                        familyPlace,
                        delivery,
                        creditCard
                )
        )
    }
}

class FilterList(
        val filters: MutableList<Boolean> = mutableListOf()
) {
    fun compare(compareTo: FilterList): Boolean {
        if (this.filters.size == compareTo.filters.size) {
            for (i in 0 until filters.size) {
                if (this.filters[i]) {
                    if (!compareTo.filters[i]) {
                        return false
                    }
                }
            }
            return true
        } else {
            return false
        }
    }
}
