package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.security.RsaKeyProperties
import hu.zsof.restaurantApp.security.SecurityService.Companion.ROLE_ADMIN
import hu.zsof.restaurantApp.security.SecurityService.Companion.ROLE_OWNER
import hu.zsof.restaurantApp.service.UserService
import hu.zsof.restaurantApp.util.AuthUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableConfigurationProperties(RsaKeyProperties::class)
@SpringBootApplication(/*exclude = [SecurityAutoConfiguration::class]*/)
class RestaurantAppApplication {
    @Bean
    fun commandLineRunner(userService: UserService): CommandLineRunner {
        return CommandLineRunner { args ->

            val testPass = AuthUtils.passwordEncoder.encode("Alma1234")
            val tester = MyUser(email = "test@test.hu", password = testPass, name = "Admin")
            val tester2 = MyUser(email = "tt@test.hu", password = testPass, name = "Owner")
            userService.createUser(tester, true)
            userService.createUser(tester2, false, isOwner = true)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RestaurantAppApplication>(*args)
}