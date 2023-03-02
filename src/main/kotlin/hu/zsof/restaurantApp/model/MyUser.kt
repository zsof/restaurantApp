package hu.zsof.restaurantApp.model

import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.model.enum.UserType
import javax.persistence.*

@Entity
class MyUser(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, updatable = false, name = "myuser_id")
        val id: Long = 0,
        var name: String = "",
        var nickName: String? = null,
        @Column(unique = true)
        var email: String = "",
        var password: String = "",
        var image: String? = null,
        var userType: UserType = UserType.USER,

        //Ha törlődnek a place-k, a usernek meg kell maradnia -->PERSIST
        //Ha törlődne a user és akarom h törlődjenek a place-ei -->REMOVE
        //Place táblában a "user"-hez van kapcsolva
        @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST])
        var places: MutableList<Place> = mutableListOf(),

        @ElementCollection
        var favPlaceIds: MutableList<Long> = mutableListOf()
)

fun MyUser.convertToDto(): UserDto {
    return UserDto(
            this.id,
            this.name,
            this.nickName,
            this.email,
            this.image,
            this.userType,
            this.favPlaceIds
    )
}

fun MutableList<MyUser>.convertToDto(): MutableList<UserDto> {
    val userDtos = mutableListOf<UserDto>()
    this.forEach {
        userDtos.add(it.convertToDto())
    }
    return userDtos
}
