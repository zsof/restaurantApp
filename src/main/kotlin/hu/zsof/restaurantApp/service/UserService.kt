package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository) {
    fun newUser(newMyUser: MyUser) = userRepository.save(newMyUser)

    fun getAllUser(): MutableList<MyUser> = userRepository.findAll()

    fun getUserById(id: Long) = userRepository.findById(id)

    fun deleteUserById(id: Long): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun deleteAllUser() = userRepository.deleteAll()
}
