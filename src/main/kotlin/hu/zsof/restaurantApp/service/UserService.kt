package hu.zsof.restaurantApp.service

import hu.zsof.restaurantApp.dto.PlaceDto
import hu.zsof.restaurantApp.dto.UserDto
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.convertToDto
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import hu.zsof.restaurantApp.security.SecurityService.Companion.ROLE_USER
import hu.zsof.restaurantApp.util.AuthUtils
import hu.zsof.restaurantApp.util.ValidationUtils
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository, private val placeRepository: PlaceRepository) {
    fun createUser(newUser: MyUser): MyUser {
        newUser.userType = ROLE_USER
        return userRepository.save(newUser)
    }

    fun getAllUser(): MutableList<MyUser> = userRepository.findAll()

    fun getUserById(id: Long): MyUser {
        val user = userRepository.findById(id)
        if (user.isPresent) {
            return user.get()
        } else {
            throw MyException("User not found", HttpStatus.NOT_FOUND)
        }
    }

    fun getUserByEmail(email: String): MyUser {
        val user = userRepository.findUserByEmail(email)
        if (user.isPresent) {
            return user.get()
        } else {
            throw MyException("User not found", HttpStatus.NOT_FOUND)
        }

    }

    fun deleteUserById(id: Long) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else {
            throw MyException("User not found", HttpStatus.NOT_FOUND)
        }
    }

    fun updateProfile(userId: Long, userUpdateProfileDto: UserUpdateProfileDto): MyUser {
        val userOptional = userRepository.findById(userId)
        if (!userOptional.isPresent) {
            throw MyException("User not found", HttpStatus.NOT_FOUND)
        }
        val updateUser = userOptional.get()



        if (!userUpdateProfileDto.email.isNullOrEmpty()) {
            if (ValidationUtils.checkEmailValidation(userUpdateProfileDto.email)) {
                updateUser.email = userUpdateProfileDto.email
            } else {
                throw MyException("Email address format is not correct", HttpStatus.BAD_REQUEST)
            }
        }

        if (!userUpdateProfileDto.password.isNullOrEmpty()) {
            if (ValidationUtils.checkPasswordValidation(userUpdateProfileDto.password)) {
                updateUser.password = userUpdateProfileDto.password
            }else {
                throw MyException("Password format is not correct", HttpStatus.BAD_REQUEST)
            }
        }

        updateUser.image = userUpdateProfileDto.image ?: updateUser.image
        updateUser.name = userUpdateProfileDto.name ?: updateUser.name
        updateUser.nickName = userUpdateProfileDto.nickName ?: updateUser.nickName

        //updateUser.isAdmin = false
        return userRepository.save(updateUser)
    }

    fun addFavPlace(userId: Long, placeId: Long): MyUser {
        val userOptional = userRepository.findById(userId)
        val placeOptional = placeRepository.findById(placeId)

        if (!userOptional.isPresent && !placeOptional.isPresent) {
            throw MyException("User or Place not found", HttpStatus.NOT_FOUND)
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

        return userRepository.save(user)

    }

    fun getFavPlaces(userId: Long): MutableList<Place> {
        val userOptional = userRepository.findById(userId)

        if (!userOptional.isPresent) {
            throw MyException("User not found", HttpStatus.NOT_FOUND)
        }

        val userFavPlaceIds = userOptional.get().favPlaceIds

        return placeRepository.findAllById(userFavPlaceIds)
    }
}