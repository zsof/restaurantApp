package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import hu.zsof.restaurantApp.util.AuthUtils
import hu.zsof.restaurantApp.util.Constants
import net.bytebuddy.utility.RandomString
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/images")
class ResourceController(private val placeRepository: PlaceRepository, private val userRepository: UserRepository) {

    @PostMapping()
    fun newPlace(
            @RequestParam("image") file: MultipartFile,
            // PlaceId or UserId
            @RequestParam("typeId") typeId: Long,
            // place or user
            @RequestParam("type") type: String,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<*> {
        println("lefutott $typeId")

        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED)
        }


        return try {
            val typeIdLong = typeId/*.replace("\"\"", "\"").toLong()*/
            var imageDirectory = ""
            var imagePathToSave = ""

            val directory = File(Constants.IMAGE_PATH)
            if (!directory.exists()) {
                directory.mkdir()
            }

            if (type == "place") {
                imageDirectory = Constants.IMAGE_PLACE_PATH
                val directoryPlaces = File(Constants.IMAGE_PLACE_PATH)
                if (!directoryPlaces.exists()) {
                    directoryPlaces.mkdir()
                }

            } else if (type == "user") {
                imageDirectory = Constants.IMAGE_USER_PATH
                val directoryPlaces = File(Constants.IMAGE_USER_PATH)
                if (!directoryPlaces.exists()) {
                    directoryPlaces.mkdir()
                }
            }

            val extension = StringUtils.getFilenameExtension(file.originalFilename)
            val newFileName = RandomString(8)
            val fileNameAndPath: Path = Paths.get(imageDirectory, "$newFileName.$extension")
            Files.write(fileNameAndPath, file.bytes)
            imagePathToSave = fileNameAndPath.toString()

            if (type == "place") {
                val placeOptional = placeRepository.findById(typeIdLong)
                if (placeOptional.isPresent) {
                    val place = placeOptional.get()
                    place.image = imagePathToSave
                    placeRepository.save(place)
                    ResponseEntity<HttpStatus>(HttpStatus.CREATED)
                }
            } else if (type == "user") {
                val userOptional = userRepository.findById(typeIdLong)
                if (userOptional.isPresent) {
                    val user = userOptional.get()
                    user.image = imagePathToSave
                    userRepository.save(user)
                    ResponseEntity<HttpStatus>(HttpStatus.CREATED)
                }
            }

            ResponseEntity<HttpStatus>(HttpStatus.OK)
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity(Response(isSuccess = false, error = "Failed to add a new image"), HttpStatus.BAD_REQUEST)
        }
    }

}