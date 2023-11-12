package hu.zsof.restaurantApp.util

import java.util.regex.Pattern

object ValidationUtils {

    private val EMAIL_REGEX_PATTERN: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    private val LETTER: Pattern = Pattern.compile("[A-z]", Pattern.CASE_INSENSITIVE)
    private val NUMBER: Pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE)

    fun checkEmailValidation(email: String): Boolean {
        return EMAIL_REGEX_PATTERN.matcher(email).find()
    }

    fun checkPasswordValidation(password: String): Boolean {
        return (
            password.length >= 6 && NUMBER.matcher(password).find() &&
                LETTER.matcher(password).find() &&
                !password.contains(" ")
            )
    }
}