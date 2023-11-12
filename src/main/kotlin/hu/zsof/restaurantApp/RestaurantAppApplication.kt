package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.security.ConfigurationProperties
import hu.zsof.restaurantApp.util.RestaurantConfigurations
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(ConfigurationProperties::class, RestaurantConfigurations::class)
@SpringBootApplication
class RestaurantAppApplication {
   /* @Bean
    fun commandLineRunner(userService: UserService): CommandLineRunner {
        return CommandLineRunner { args ->

            val testPass = ValidationUtils.passwordEncoder.encode("Alma1234")
            val owner = MyUser(email = "tt@test.hu", password = testPass, name = "Owner")
            val user = MyUser(email = "t@test.hu", password = testPass, name = "User")
            userService.createUser(owner, isOwner = true)
            userService.createUser(user, isOwner = false)
            userService.verifyEmail(owner.id, owner.verificationSecret)
            userService.verifyEmail(user.id, user.verificationSecret)
        }
    }*/
}

fun main(args: Array<String>) {
    runApplication<RestaurantAppApplication>(*args)
}