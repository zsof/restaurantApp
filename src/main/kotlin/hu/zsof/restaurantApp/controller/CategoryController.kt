package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.service.CategoryService
import hu.zsof.restaurantApp.model.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/categories")
class CategoryController @Autowired constructor(private val categoryService: CategoryService) {

    @PostMapping()
    fun newCategory(@RequestBody category: Category): ResponseEntity<Category> {
        val newCategory = categoryService.newCategory(category)
        return ResponseEntity(newCategory, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Int): ResponseEntity<Category?> {
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
}