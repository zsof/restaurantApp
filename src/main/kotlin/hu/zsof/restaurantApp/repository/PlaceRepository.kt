package hu.zsof.restaurantApp.repository

import hu.zsof.restaurantApp.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
interface PlaceRepository : JpaRepository<Place, Int>