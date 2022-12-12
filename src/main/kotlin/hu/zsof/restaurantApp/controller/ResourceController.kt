package hu.zsof.restaurantApp.controller

import hu.zsof.restaurantApp.model.response.Response
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import hu.zsof.restaurantApp.util.AuthUtils
import hu.zsof.restaurantApp.util.Constants
import net.bytebuddy.utility.RandomString
import org.springframework.core.io.UrlResource
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
            @RequestParam("typeId") typeId: String,
            // place or user
            @RequestParam("type") type: String,
            @CookieValue(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<*> {
        val typeIdLong = typeId.trim().replace("\"", "").toLongOrNull()
        val trimmedType = type.trim().replace("\"", "")

        if (typeIdLong == null) {
            return ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST)
        }

        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED)
        }

        return try {
            var imageDirectory = ""
            var imageDirectoryName = ""
            var imagePathToSave = ""

            val directory = File(Constants.IMAGE_PATH)
            if (!directory.exists()) {
                directory.mkdir()
            }

            if (trimmedType == "place") {
                imageDirectory = Constants.IMAGE_PLACE_PATH
                imageDirectoryName = Constants.IMAGE_PLACE_PATH_NAME
                val directoryPlaces = File(imageDirectory)
                if (!directoryPlaces.exists()) {
                    directoryPlaces.mkdir()
                }

            } else if (trimmedType == "user") {
                imageDirectory = Constants.IMAGE_USER_PATH
                imageDirectoryName = Constants.IMAGE_USER_PATH_NAME
                val directoryPlaces = File(Constants.IMAGE_USER_PATH)
                if (!directoryPlaces.exists()) {
                    directoryPlaces.mkdir()
                }
            }

            val extension = StringUtils.getFilenameExtension(file.originalFilename)
            if (extension == "jpg" || extension == "png") {
                val newFileName = RandomString.make(8)
                val fileNameAndPath: Path = Paths.get(imageDirectory, "$newFileName.$extension")
                Files.write(fileNameAndPath, file.bytes)
                imagePathToSave = "$imageDirectoryName-$newFileName.$extension"
            }

            if (trimmedType == "place") {
                val placeOptional = placeRepository.findById(typeIdLong)
                if (placeOptional.isPresent) {

                    val place = placeOptional.get()
                    place.image = imagePathToSave
                    placeRepository.save(place)
                    ResponseEntity<HttpStatus>(HttpStatus.CREATED)
                }
            } else if (trimmedType == "user") {
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

    @GetMapping()
    fun getResource(
            @RequestParam("image") imagePath: String,
            @RequestParam(AuthUtils.COOKIE_NAME) token: String?
    ): ResponseEntity<*> { // UrlResource + HttpStatus

        val verification = AuthUtils.verifyToken(token)
        if (!verification.verified) {
            return ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED)
        }

        val extension = StringUtils.getFilenameExtension(imagePath)
        val splits = imagePath.split('-')
        val directory = splits[0]
        val filename = splits[1]
        if (directory != "users" && directory != "places") {
            return ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST)
        }
        val path: Path = Paths.get("images/$directory/$filename")

        val image = File(path.toUri())
        if (!image.exists()) {
            return ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND)
        }

        val urlRes = UrlResource(path.toUri())
        val contentType: MediaType = if (extension == "jpg") MediaType.IMAGE_JPEG else MediaType.IMAGE_PNG

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(urlRes)
    }
}