package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(private val userRepository: UserRepository) {
    fun createUser(newUser: MyUser): MyUser {
        newUser.isAdmin = false
        return userRepository.save(newUser)
    }

    fun getAllUser(): MutableList<MyUser> = userRepository.findAll()

    fun getUserById(id: Long) = userRepository.findById(id)
    fun getUserByEmail(email: String) = userRepository.findUserByEmail(email)

    fun deleteUserById(id: Long) = userRepository.deleteById(id)

    fun deleteAllUser() = userRepository.deleteAll()

    fun updateProfile(userId: Long, userUpdateProfileDto: UserUpdateProfileDto): Optional<UserDto> {
        val userOptional = userRepository.findById(userId)
        if (!userOptional.isPresent) {
            return Optional.empty()
        }
        val updateUser = userOptional.get()
        updateUser.password = userUpdateProfileDto.password ?: updateUser.password
        updateUser.image = userUpdateProfileDto.image ?: updateUser.image
        updateUser.name = userUpdateProfileDto.name ?: updateUser.name
        updateUser.nickName = userUpdateProfileDto.nickName ?: updateUser.nickName

        //updateUser.isAdmin = false
        return Optional.of(userRepository.save(updateUser).convertToDto())
    }

    fun addFavPlace(userId: Long, place: Place): Optional<UserDto> { //??
        val userOptional = userRepository.findById(userId)
        if (!userOptional.isPresent) {
            return Optional.empty()
        }
        val user = userOptional.get()
        user.favPlaces.add(place)

        return Optional.of(userRepository.save(user).convertToDto())
    }
}
