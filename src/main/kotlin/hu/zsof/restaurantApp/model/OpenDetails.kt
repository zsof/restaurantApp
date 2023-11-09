package hu.zsof.restaurantApp.model

import javax.persistence.Embeddable

@Embeddable
class OpenDetails(
    //  @DateTimeFormat(pattern = "HH:mm:ss")

    var basicOpen: String = "",
    var basicClose: String = "",
    var mondayOpen: String = "",
    var mondayClose: String = "",
    var tuesdayOpen: String = "",
    var tuesdayClose: String = "",
    var wednesdayOpen: String = "",
    var wednesdayClose: String = "",
    var thursdayOpen: String = "",
    var thursdayClose: String = "",
    var fridayOpen: String = "",
    var fridayClose: String = "",
    var saturdayOpen: String = "",
    var saturdayClose: String = "",
    var sundayOpen: String = "",
    var sundayClose: String = "",

    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
    var sunday: Boolean = false,
)