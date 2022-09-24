package hu.zsof.restaurantApp.repository

import hu.zsof.restaurantApp.model.MyUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<MyUser, Long>
