package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.service.PlaceService
import org.hibernate.service.spi.InjectService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RestaurantAppApplication

fun main(args: Array<String>) {
    runApplication<RestaurantAppApplication>(*args)
}
