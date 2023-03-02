package hu.zsof.restaurantApp.exception

import org.springframework.http.HttpStatus

class MyException (override val message: String, val statusCode: HttpStatus) : Exception()