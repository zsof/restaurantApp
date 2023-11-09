package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
class AdminController(private val userService: UserService) {

    /**
     * User controller functions
     */
    @GetMapping("users/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): ResponseEntity<UserDto?> {
        return ResponseEntity(userService.getUserById(id).convertToDto(), HttpStatus.OK)
    }

    @DeleteMapping("users/{id}")
    fun deleteUserById(
        @PathVariable id: Long,
    ): ResponseEntity<Response> {
        userService.deleteUserById(id)
        return ResponseEntity(Response(true), HttpStatus.OK)
    }

    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<UserDto>> {
        val users: MutableList<MyUser> = userService.getAllUser()
        return ResponseEntity<List<UserDto>>(users.convertToDto(), HttpStatus.OK)
    }
}