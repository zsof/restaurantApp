package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(private val userRepository: UserRepository, private val placeRepository: PlaceRepository) {
    fun createUser(newUser: MyUser): MyUser {
        newUser.isAdmin = false
        return userRepository.save(newUser)
    }

    fun getAllUser(): MutableList<MyUser> = userRepository.findAll()

    fun getUserById(id: Long): Optional<MyUser> = userRepository.findById(id)
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
        updateUser.email = userUpdateProfileDto.email ?: updateUser.email

        //updateUser.isAdmin = false
        return Optional.of(userRepository.save(updateUser).convertToDto())
    }

    fun addFavPlace(userId: Long, placeId: Long): Optional<UserDto> {
        val userOptional = userRepository.findById(userId)
        val placeOptional = placeRepository.findById(placeId)

        if (!userOptional.isPresent && !placeOptional.isPresent) {
            return Optional.empty()
        }

        val user = userOptional.get()
        val place = placeOptional.get()

        val alreadyFav = user.favPlaceIds.contains(placeId)

        if (alreadyFav) {
            user.favPlaceIds.remove(placeId)
            place.usersNumber = place.usersNumber - 1
        } else {
            user.favPlaceIds.add(placeId)
            place.usersNumber = place.usersNumber + 1
        }
        placeRepository.save(place)

        return Optional.of(userRepository.save(user).convertToDto())

    }

    fun getFavPlaces(userId: Long): Optional<List<PlaceDto>> {
        val userOptional = userRepository.findById(userId)

        if (!userOptional.isPresent) {
            return Optional.empty()
        }

        val userFavPlaceIds = userOptional.get().favPlaceIds
        val places: MutableList<PlaceDto> = placeRepository.findAllById(userFavPlaceIds).convertToDto()

        return Optional.of(places)
    }
}
