package hu.zsof.restaurantApp.exception

import hu.zsof.restaurantApp.model.response.Response
import org.springframework.core.NestedExceptionUtils
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MyException::class)
    fun handleMyException(ex: MyException): ResponseEntity<Response> {
        return ResponseEntity<Response>(Response(error = ex.message, isSuccess = false), ex.statusCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedErrorException(ex: Exception): ResponseEntity<Response> {
        ex.printStackTrace()
        return ResponseEntity<Response>(
                Response(error = ex.message.toString(), isSuccess = false),
                HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUserNameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<Response> {
        return ResponseEntity<Response>(
                Response(error = "Username (email) not found.", isSuccess = false),
                HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityException(ex: DataIntegrityViolationException): ResponseEntity<Response> {
        val message = NestedExceptionUtils.getMostSpecificCause(ex).message.toString()
        return ResponseEntity<Response>(
                Response(error = message, isSuccess = false), HttpStatus.CONFLICT)
    }
}