package hu.zsof.restaurantApp.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@ConfigurationProperties(prefix = "rsa")
class ConfigurationProperties {
    @Value("\${rsa.private-key}")
    var rsaPrivateKey: RSAPrivateKey? = null

    @Value("\${rsa.public-key}")
    var rsaPublicKey: RSAPublicKey? = null

    @Value("classpath:html/register.email.html")
    var resource: Resource? = null
}