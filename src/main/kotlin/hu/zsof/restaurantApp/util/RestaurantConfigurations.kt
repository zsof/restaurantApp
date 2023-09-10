package hu.zsof.restaurantApp.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties
class RestaurantConfigurations {

    @Value("classpath:html/register.email.html")
    var verifyTemplate: Resource? = null
}