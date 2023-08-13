package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.security.RsaKeyProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(RsaKeyProperties::class)
@SpringBootApplication(/*exclude = [SecurityAutoConfiguration::class]*/)
class RestaurantAppApplication

fun main(args: Array<String>) {
    runApplication<RestaurantAppApplication>(*args)


}