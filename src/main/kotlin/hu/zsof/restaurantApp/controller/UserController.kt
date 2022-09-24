package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping()
    fun newUser(@RequestBody user: MyUser): ResponseEntity<UserDto> {
        val newUser = userService.newUser(user)
        return ResponseEntity(newUser.convertToDto(), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDto?> {
        val user: Optional<MyUser> = userService.getUserById(id)
        if (!user.isPresent) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(user.get().convertToDto(), HttpStatus.OK)
    }

    @GetMapping
    fun getAllUser(): ResponseEntity<List<UserDto>> {
        val users: MutableList<MyUser> = userService.getAllUser()
        return ResponseEntity<List<UserDto>>(users.convertToDto(), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<HttpStatus> {
        return if (userService.deleteUserById(id)) {
            ResponseEntity(HttpStatus.OK)
        } else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping
    fun deleteAll(): ResponseEntity<HttpStatus> {
        userService.deleteAllUser()

        return ResponseEntity(HttpStatus.OK)
    }
}
