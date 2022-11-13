package hu.zsof.restaurantApp.model

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.model.enum.Type
import hu.zsof.restaurantApp.model.enum.Price
import javax.persistence.*

@Entity
class Place(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, updatable = false, name = "place_id")
        val id: Long = 0,

        var name: String = "",

        var address: String? = null,
        var rate: Float? = 2.0f,
        var price: Price = Price.LOW,
        var image: String? = null,
        var phoneNumber: String? = null,
        var email: String? = null,
        var web: String? = null,

        // accepted: Boolean = false todo

        var type: Type = Type.RESTAURANT,

        @Embedded
        var filter: Filter = Filter(),

        @ManyToMany(mappedBy = "favPlaces")
        val users: MutableList<MyUser> = mutableListOf()
)

fun Place.convertToDto(): PlaceDto {
    return PlaceDto(
            this.id,
            this.name,
            this.address,
            this.rate,
            this.price,
            this.image,
            this.type,
            this.filter,
            this.phoneNumber,
            this.email,
            this.web
    )
}

fun MutableList<Place>.convertToDto(): MutableList<PlaceDto> {
    val placeDtos = mutableListOf<PlaceDto>()
    this.forEach {
        placeDtos.add(it.convertToDto())
    }
    return placeDtos
}
