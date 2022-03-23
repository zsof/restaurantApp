package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.model.Category
import hu.zsof.restaurantApp.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class CategoryService @Autowired constructor(private val categoryRepository: CategoryRepository){

    fun newCategory(newCategory: Category): Category {
        return categoryRepository.save(newCategory)
    }

    fun getAllCategory() : MutableList<Category>{
        return categoryRepository.findAll()
    }

    fun getCategoryById(id :Int): Optional<Category> {
        return categoryRepository.findById(id)
    }
}