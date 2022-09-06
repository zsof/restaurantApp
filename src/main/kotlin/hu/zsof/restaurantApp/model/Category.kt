package hu.zsof.restaurantApp.model

import javax.persistence.*

@Entity
data class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Long,
    val name: String? = null,

  /*  @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    val place: Set<Place>? = null */
)
