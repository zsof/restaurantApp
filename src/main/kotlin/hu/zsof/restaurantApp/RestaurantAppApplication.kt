package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.security.RsaKeyProperties
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableConfigurationProperties(RsaKeyProperties::class)
@SpringBootApplication
class RestaurantAppApplication {
    @Bean
    fun commandLineRunner(userService: UserService): CommandLineRunner {
        return CommandLineRunner { args ->

            val testPass = AuthUtils.passwordEncoder.encode("Alma1234")
            val admin = MyUser(email = "test@test.hu", password = testPass, name = "Admin")
            val owner = MyUser(email = "tt@test.hu", password = testPass, name = "Owner")
            val user = MyUser(email = "t@test.hu", password = testPass, name = "User")
            userService.createUser(admin, true)
            userService.createUser(owner, false, isOwner = true)
            userService.createUser(user, false, isOwner = false)
            userService.verifyEmail(admin.id, admin.verificationSecret)
            userService.verifyEmail(owner.id, owner.verificationSecret)
            userService.verifyEmail(user.id, user.verificationSecret)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RestaurantAppApplication>(*args)
}