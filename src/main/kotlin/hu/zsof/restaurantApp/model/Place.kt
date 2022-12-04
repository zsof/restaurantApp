package hu.zsof.restaurantApp.model

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.PlaceMapDto
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

        var address: String = "",
        var rate: Float? = 2.0f,
        var price: Price = Price.LOW,
        var image: String? = null,
        var phoneNumber: String? = null,
        var email: String? = null,
        var web: String? = null,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var usersNumber: Int = 0,

        var type: Type = Type.RESTAURANT,

        @Embedded
        var filter: Filter = Filter(),
        @Embedded
        var openDetails: OpenDetails = OpenDetails()
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
            this.openDetails,
            this.phoneNumber,
            this.email,
            this.web,
            this.usersNumber
    )
}

fun MutableList<Place>.convertToDto(): MutableList<PlaceDto> {
    val placeDtos = mutableListOf<PlaceDto>()
    this.forEach {
        placeDtos.add(it.convertToDto())
    }
    return placeDtos
}

fun Place.convertToPlaceMapDto(): PlaceMapDto {
    return PlaceMapDto(
            this.id,
            this.name,
            this.address,
            this.latitude,
            this.longitude
    )
}

fun MutableList<Place>.convertToPlaceMapDto(): MutableList<PlaceMapDto> {
    val placeMapDtos = mutableListOf<PlaceMapDto>()
    this.forEach {
        placeMapDtos.add(it.convertToPlaceMapDto())
    }
    return placeMapDtos
}
