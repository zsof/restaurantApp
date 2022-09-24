package hu.zsof.restaurantApp.model.enum

enum class Category {
    RESTAURANT,
    CAFE,
    PATISSERIE
}

/*@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(nullable = false, updatable = false)
val id: Long,
val name: String? = null*/

/*  @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
  val place: Set<Place>? = null */

