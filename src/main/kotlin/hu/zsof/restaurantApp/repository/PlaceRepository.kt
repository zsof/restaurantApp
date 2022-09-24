package hu.zsof.restaurantApp.repository

import hu.zsof.restaurantApp.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaceRepository : JpaRepository<Place, Long>
