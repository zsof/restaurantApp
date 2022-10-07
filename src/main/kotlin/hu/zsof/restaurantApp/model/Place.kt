package hu.zsof.restaurantApp.model

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.model.enum.Category
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
    val price: Float? = 2.0f,
    val image: String? = null,

    //accepted: Boolean = false todo

    val category: Category = Category.RESTAURANT,

    @Embedded
    val filter: Filter = Filter(),

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
        this.category,
        this.filter
    )
}

fun MutableList<Place>.convertToDto(): MutableList<PlaceDto> {
    val placeDtos = mutableListOf<PlaceDto>()
    this.forEach {
        placeDtos.add(it.convertToDto())
    }
    return placeDtos
}
