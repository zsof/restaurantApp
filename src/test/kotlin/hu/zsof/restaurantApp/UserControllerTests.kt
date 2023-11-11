package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.controller.UserController
import hu.zsof.restaurantApp.dto.UserUpdateProfileDto
import hu.zsof.restaurantApp.model.Filter
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import hu.zsof.restaurantApp.service.MailService
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
class UserControllerTests {

    private lateinit var controller: UserController

    private lateinit var authentication: JwtAuthenticationToken

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var placeRepository: PlaceRepository

    @Mock
    private lateinit var mailService: MailService

    @BeforeEach
    fun setUp() {
        val userService = UserService(
            userRepository = userRepository,
            placeRepository = placeRepository,
            mailService = mailService,
        )

        controller = UserController(
            userService = userService,
        )

        authentication = JwtAuthenticationToken(
            mock(Jwt::class.java),
            listOf(mock(SimpleGrantedAuthority::class.java)),
            "1",
        )
    }

    @Test
    fun testGetUserProfile() {
        // Arrange
        `when`(userRepository.findById(anyLong())).thenReturn(Optional.of(MyUser(id = 1, name = "Alma")))

        // Act
        val response = controller.getUserProfile(authentication)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Alma", response.body?.name)
    }

    @Test
    fun testUpdateUserProfile() {
        // Arrange
        `when`(userRepository.findById(anyLong())).thenReturn(Optional.of(MyUser(id = 1, name = "Alma")))
        `when`(userRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.updateProfile(
            UserUpdateProfileDto(name = "Béla", filters = Filter(glutenFree = true), image = "PxcF23D_ew"),
            authentication,
        )

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Béla", response.body?.name)
        assertNotEquals("Alma", response.body?.name)
        assertEquals(true, response.body?.filterItems?.glutenFree)
        assertNotEquals(true, response.body?.filterItems?.dogFriendly)
    }

    @Test
    fun testGetUserFavPlaces() {
        // Arrange
        `when`(userRepository.findById(anyLong())).thenReturn(
            Optional.of(
                MyUser(
                    id = 1,
                    name = "Alma",
                    favPlaceIds = mutableListOf(1, 2),
                ),
            ),
        )
        `when`(placeRepository.findAllById(mutableListOf(1, 2))).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó"),
                Place(id = 2, name = "Körte Étterem"),
            ),
        )

        // Act
        val response = controller.getUserFavPlaces(authentication)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun testAddFavPlaces() {
        // Arrange
        `when`(userRepository.findById(anyLong())).thenReturn(Optional.of(MyUser(id = 1, favPlaceIds = mutableListOf(1, 2))))
        `when`(placeRepository.findById(anyLong())).thenReturn(Optional.of(Place(id = 1)))
        `when`(placeRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }
        `when`(userRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.addFavPlaceForUser(1, authentication)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
    }
}