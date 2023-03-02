package hu.zsof.restaurantApp.security

import hu.zsof.restaurantApp.model.MyUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserSecurity (private val user: MyUser) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val auths: MutableList<SimpleGrantedAuthority> = ArrayList()
        auths.add(SimpleGrantedAuthority(user.userType.toString()))
        return auths
    }

    override fun getPassword(): String {
        return user.password
    }

    // email - unique
    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}