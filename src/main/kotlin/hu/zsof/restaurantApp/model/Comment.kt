package hu.zsof.restaurantApp.model

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,
        var message: String = "",
        var userId: Long = -1,
        var userName: String = "",
        var createDate: Instant,
        var placeId: Long = -1
)