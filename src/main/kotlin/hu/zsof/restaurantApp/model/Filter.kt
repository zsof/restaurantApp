package hu.zsof.restaurantApp.model

import javax.persistence.*

@Embeddable
class Filter(

        private var glutenFree: Boolean = false,
        private var lactoseFree: Boolean = false,
        private var vegetarian: Boolean = false,
        private var vegan: Boolean = false,
        private var fastFood: Boolean = false,

        private var parkingAvailable: Boolean = false,
        private var dogFriendly: Boolean = false,
        private var familyPlace: Boolean = false,
        private var delivery: Boolean = false,
        private var creditCard: Boolean = false,
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
        private val filters: MutableList<Boolean> = mutableListOf()
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
