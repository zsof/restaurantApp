package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.controller.PlaceByUserController
import hu.zsof.restaurantApp.dto.FilterDto
import hu.zsof.restaurantApp.model.Comment
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.request.CommentData
import hu.zsof.restaurantApp.repository.CommentRepository
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import hu.zsof.restaurantApp.service.CommentService
import hu.zsof.restaurantApp.service.MailService
import hu.zsof.restaurantApp.service.PlaceService
import hu.zsof.restaurantApp.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

@ExtendWith(MockitoExtension::class)
class PlaceByUserControllerTests {

    private lateinit var controller: PlaceByUserController

    private lateinit var placeService: PlaceService

    private lateinit var authentication: JwtAuthenticationToken

    @Mock
    private lateinit var placeRepository: PlaceRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var commentRepository: CommentRepository

    @Mock
    private lateinit var mailService: MailService

    @BeforeEach
    fun setUp() {
        placeService = PlaceService(placeRepository = placeRepository)
        val userService = UserService(
            userRepository = userRepository,
            placeRepository = placeRepository,
            mailService = mailService,
        )
        val commentService = CommentService(
            commentRepository = commentRepository,
            placeService = placeService,
        )

        controller = PlaceByUserController(
            placeService = placeService,
            userService = userService,
            commentService = commentService,
        )

        authentication = JwtAuthenticationToken(
            mock(Jwt::class.java),
            listOf(mock(SimpleGrantedAuthority::class.java)),
            "1",
        )
    }

    @Test
    fun testPlaceGetById() {
        // Arrange
        `when`(placeRepository.findById(anyLong())).thenReturn(Optional.of(Place(id = 1, name = "Alma Fagyizó")))

        // Act
        val response = controller.getPlaceById(1)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Alma Fagyizó", response.body?.name)
    }

    @Test
    fun testGetAllPlace() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó"),
                Place(id = 2, name = "Körte Étterem"),
            ),
        )

        // Act
        val response = controller.getAllPlace()

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Körte Étterem", response.body?.get(1)?.name)
    }

    @Test
    fun testGetAllPlaceInMap() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó"),
                Place(id = 2, name = "Körte Étterem"),
            ),
        )

        // Act
        val response = controller.getAllPlacesInMap()

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotEquals("Körte Étterem", response.body?.get(0)?.name)
        assertEquals("Körte Étterem", response.body?.get(1)?.name)
    }

    @Test
    fun testFilterPlace() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(listOf(Place(id = 1, name = "Alma Fagyizó")))

        // Act
        val response = controller.filterPlace(FilterDto())

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun testGetCommentsByPlaceId() {
        // Arrange
        `when`(commentRepository.findAllByPlaceId(placeId = 1)).thenReturn(
            listOf(
                Comment(id = 1, message = "Jó hely!", placeId = 1),
                Comment(id = 2, message = "Szuper", placeId = 1),
            ),
        )

        // Act
        val response = controller.getCommentsByPlaceId(1)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotEquals("Szuper", response.body?.get(0)?.message)
        assertEquals("Szuper", response.body?.get(1)?.message)
    }

    @Test
    fun textNewComment() {
        // Arrange
        `when`(placeRepository.findById(anyLong())).thenReturn(Optional.of(Place()))
        `when`(commentRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.newComment(CommentData("Szuper", 1), authentication)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun testDeleteCommentByID() {
        // Arrange
        doNothing().`when`(commentRepository).deleteById(anyLong())
        `when`(commentRepository.existsById(anyLong())).thenReturn((true))

        // Act
        val response = controller.deleteCommentById(1)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.isSuccess)
    }
}