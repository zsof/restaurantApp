package hu.zsof.restaurantApp.security

import hu.zsof.restaurantApp.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserSecurityDetailService(private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        return userRepository.findUserByEmail(email)
                .map { UserSecurity(it) }
                .orElseThrow { UsernameNotFoundException("User not found: $email") }
    }
}