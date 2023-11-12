package hu.zsof.restaurantApp.util

import java.io.File

object ResourceUtil {
    fun deleteImage(imagePath: String?) {
        // delete image if exists
        if (imagePath != null && imagePath != "") {
            val splits = imagePath.split('-')
            val directoryName = splits[0]
            val fileName = splits[1]
            val imageFile = File("images/$directoryName/$fileName")
            if (imageFile.exists()) {
                imageFile.delete()
            }
        }
    }
}