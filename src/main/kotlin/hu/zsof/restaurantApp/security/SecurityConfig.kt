package hu.zsof.restaurantApp.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import hu.zsof.restaurantApp.exception.MyException
import hu.zsof.restaurantApp.security.SecurityService.Companion.CLAIM_ROLE
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import java.time.Instant
import java.util.stream.Collectors

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
class SecurityConfig2(
    private val securityUserDetailService: UserSecurityDetailService,
    private val configurationProperties: ConfigurationProperties,
) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/v2/api-docs").permitAll() // for swagger
            .antMatchers("/swagger-resources/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer { httpSecurityOAuth2ResourceServerConfigurer: OAuth2ResourceServerConfigurer<HttpSecurity?> ->
                httpSecurityOAuth2ResourceServerConfigurer.jwt()
                    .jwtAuthenticationConverter(jwtAuthConverter())
            }
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .userDetailsService(securityUserDetailService)
            .httpBasic(Customizer.withDefaults<HttpBasicConfigurer<HttpSecurity>>())
            .exceptionHandling { e ->
                e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.I_AM_A_TEAPOT))
            }
            .build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(configurationProperties.rsaPublicKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val rsaKey = RSAKey.Builder(configurationProperties.rsaPublicKey).privateKey(configurationProperties.rsaPrivateKey).build()
        val jwkSource = ImmutableJWKSet<SecurityContext>(JWKSet(rsaKey))

        return NimbusJwtEncoder(jwkSource)
    }

    private fun jwtAuthConverter(): Converter<Jwt, out AbstractAuthenticationToken> {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(JwtRoleConverter())

        return jwtAuthenticationConverter
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}

class JwtRoleConverter : Converter<Jwt?, Collection<GrantedAuthority?>?> {
    override fun convert(source: Jwt): Collection<GrantedAuthority> {
        val roles = source.getClaimAsStringList(CLAIM_ROLE)

        if (source.expiresAt == null || source.expiresAt?.isBefore(Instant.now()) == true) {
            throw MyException("Jwt Expired", HttpStatus.UNAUTHORIZED)
        }

        return roles.stream().map { role: String? ->
            SimpleGrantedAuthority(role)
        }.collect(Collectors.toList())
    }
}