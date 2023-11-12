package hu.zsof.restaurantApp.model

import hu.zsof.restaurantApp.dto.PlaceInReviewDto
import hu.zsof.restaurantApp.model.enum.Price
import hu.zsof.restaurantApp.model.enum.Type
import javax.persistence.*

@Entity
class PlaceInReview(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "place_inreview_id")
    val id: Long = 0,

    var name: String = "",

    var address: String = "",
    var price: Price = Price.LOW,
    var image: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var web: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var type: Type = Type.RESTAURANT,

    // Sok place-je lehet egy usernek -> MyUser táblában meg @OneToMany kell
    @ManyToOne
    var creator: MyUser = MyUser(),

    var problem: String? = null,

    @Embedded
    var filter: Filter = Filter(),
    @Embedded
    var openDetails: OpenDetails = OpenDetails(),
) {
    fun convertToPlace(): Place {
        return Place(
            name = this.name,
            address = this.address,
            price = this.price,
            image = this.image,
            type = this.type,
            filter = this.filter,
            openDetails = this.openDetails,
            phoneNumber = this.phoneNumber,
            email = this.email,
            web = this.web,
            latitude = this.latitude,
            longitude = this.longitude,
            creator = MyUser(id = this.creator.id),
        )
    }
}

fun PlaceInReview.convertToDto(): PlaceInReviewDto {
    return PlaceInReviewDto(
        id = this.id,
        name = this.name,
        address = this.address,
        price = this.price,
        image = this.image,
        type = this.type,
        filter = this.filter,
        openDetails = this.openDetails,
        phoneNumber = this.phoneNumber,
        email = this.email,
        web = this.web,
        problem = this.problem,
        creatorName = this.creator.name,
        creatorId = this.creator.id,
        latitude = this.latitude,
        longitude = this.longitude,
    )
}

fun MutableList<PlaceInReview>.convertToDto(): MutableList<PlaceInReviewDto> {
    val placeDtos = mutableListOf<PlaceInReviewDto>()
    this.forEach {
        placeDtos.add(it.convertToDto())
    }
    return placeDtos
}