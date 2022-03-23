package hu.zsof.restaurantApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@SpringBootApplication
class RestaurantAppApplication

fun main(args: Array<String>) {
	runApplication<RestaurantAppApplication>(*args)
}

/*
@Bean
fun corsFilter() : CorsFilter {
	val corsConfiguration = CorsConfiguration()
	corsConfiguration.allowCredentials = true
	corsConfiguration.allowedOrigins = listOf("http://localhost:4200")
	corsConfiguration.allowedHeaders =
		listOf(
			"Origin", "Access-Control-Allow-Origin", "Content-Type",
			"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
			"Access-Control-Request-Method", "Access-Control-Request-Headers"
		)
	corsConfiguration.exposedHeaders = listOf(
		"Origin", "Content-Type", "Accept", "Authorization",
		"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
	)
	corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
	val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
	urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)
	return CorsFilter(urlBasedCorsConfigurationSource)
}*/