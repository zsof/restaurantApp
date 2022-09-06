package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.model.Category
import hu.zsof.restaurantApp.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun newCategory(newCategory: Category) = categoryRepository.save(newCategory)

    //mutablelist -->list
    fun getAllCategory(): List<Category> = categoryRepository.findAll()

    //Optional<Category>
    fun getCategoryById(id: Long) = categoryRepository.findById(id)

    fun deleteById(id: Long): Boolean {
        return if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun deleteAll() = categoryRepository.deleteAll()
}