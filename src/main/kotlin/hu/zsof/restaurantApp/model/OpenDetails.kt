package hu.zsof.restaurantApp.model

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalTime
import javax.persistence.Embeddable

@Embeddable
class OpenDetails(
        //  @DateTimeFormat(pattern = "HH:mm:ss")

        var basicOpen: LocalTime? = LocalTime.MIN,
        var basicClose: LocalTime? = LocalTime.MIN,
        var mondayOpen: LocalTime? = LocalTime.MIN,
        var mondayClose: LocalTime? = LocalTime.MIN,
        var tuesdayOpen: LocalTime? = LocalTime.MIN,
        var tuesdayClose: LocalTime? = LocalTime.MIN,
        var wednesdayOpen: LocalTime? = LocalTime.MIN,
        var wednesdayClose: LocalTime? = LocalTime.MIN,
        var thursdayOpen: LocalTime? = LocalTime.MIN,
        var thursdayClose: LocalTime? = LocalTime.MIN,
        var fridayOpen: LocalTime? = LocalTime.MIN,
        var fridayClose: LocalTime? = LocalTime.MIN,
        var saturdayOpen: LocalTime? = LocalTime.MIN,
        var saturdayClose: LocalTime? = LocalTime.MIN,
        var sundayOpen: LocalTime? = LocalTime.MIN,
        var sundayClose: LocalTime? = LocalTime.MIN,

        var monday: Boolean = false,
        var tuesday: Boolean = false,
        var wednesday: Boolean = false,
        var thursday: Boolean = false,
        var friday: Boolean = false,
        var saturday: Boolean = false,
        var sunday: Boolean = false
)
