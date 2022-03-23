package hu.zsof.restaurantApp.model

import javax.persistence.*


@Entity
data class Place (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Int,

    val name: String? = null,

    val address: String? = null,

    @ManyToOne
    val category: Category,


    @Embedded
    val filters: Filter

)
