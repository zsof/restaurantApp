package hu.zsof.restaurantApp.repository

import hu.zsof.restaurantApp.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<Category, Int>