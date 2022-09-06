package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.service.CategoryService
import hu.zsof.restaurantApp.model.Category
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {

    @PostMapping()
    fun newCategory(@RequestBody category: Category): ResponseEntity<Category> {
        val newCategory = categoryService.newCategory(category)
        return ResponseEntity(newCategory, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Category?> {
        val category: Optional<Category> = categoryService.getCategoryById(id)
        if (!category.isPresent)
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        return ResponseEntity(category.get(), HttpStatus.OK)
    }

    @GetMapping
    fun getAllCategory(): ResponseEntity<List<Category>> {
        val categories: List<Category> = categoryService.getAllCategory()
        return ResponseEntity<List<Category>>(categories, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<HttpStatus> {
        return if (categoryService.deleteById(id)) {
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping
    fun deleteAll() : ResponseEntity<HttpStatus> {
        categoryService.deleteAll()
        return ResponseEntity(HttpStatus.OK)
    }
}