package hu.zsof.restaurantApp.model

import javax.persistence.*


@Entity
data class Place (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Long,

    val name: String? = null,

    val address: String? = null,
    val rate: Float? = 2.0f,
    val price: Float? = 2.0f,
    val image: String? = null,

    @ManyToOne
    val category: Category,

    @Embedded
    val filters: Filter

)
