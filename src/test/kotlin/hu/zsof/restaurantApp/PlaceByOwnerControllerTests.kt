package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.controller.PlaceByOwnerController
import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.Place
import hu.zsof.restaurantApp.model.PlaceInReview
import hu.zsof.restaurantApp.repository.PlaceInReviewRepository
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.service.PlaceInReviewService
import hu.zsof.restaurantApp.service.PlaceService
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
class PlaceByOwnerControllerTests {

    private lateinit var controller: PlaceByOwnerController

    private lateinit var authentication: JwtAuthenticationToken

    @Mock
    private lateinit var placeRepository: PlaceRepository

    @Mock
    private lateinit var placeInReviewRepository: PlaceInReviewRepository

    @BeforeEach
    fun setUp() {
        val placeService = PlaceService(placeRepository = placeRepository)
        val placeInReviewService = PlaceInReviewService(
            placeInReviewRepository = placeInReviewRepository,
            placeService = placeService,
            placeRepository = placeRepository,
        )

        controller = PlaceByOwnerController(
            placeService = placeService,
            placeInReviewService = placeInReviewService,
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
        `when`(placeInReviewRepository.findById(anyLong())).thenReturn(Optional.of(PlaceInReview(id = 1, name = "Alma Fagyizó")))

        // Act
        val response = controller.getPlaceById(1)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Alma Fagyizó", response.body?.name)
    }

    @Test
    fun testGetAllPlaceByOwner() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó", creator = MyUser(id = 1)),
                Place(id = 2, name = "Körte Étterem", creator = MyUser(id = 1)),
            ),
        )

        // Act
        val response = controller.getAllPlaceByOwner(authentication)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotEquals("Körte Étterem", response.body?.get(0)?.name)
        assertEquals("Körte Étterem", response.body?.get(1)?.name)
    }

    @Test
    fun testGetAllPlaceInReviewByOwner() {
        // Arrange
        `when`(placeInReviewRepository.findAll()).thenReturn(
            listOf(
                PlaceInReview(id = 1, name = "Alma Fagyizó", user = MyUser(id = 1)),
                PlaceInReview(id = 2, name = "Körte Étterem", user = MyUser(id = 1)),
            ),
        )

        // Act
        val response = controller.getAllPlaceInReviewByOwner(authentication)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotEquals("Körte Étterem", response.body?.get(0)?.name)
        assertEquals("Körte Étterem", response.body?.get(1)?.name)
    }

    @Test
    fun textNewComment() {
        // Arrange
        `when`(placeInReviewRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.newPlace(PlaceInReview(id = 1, name = "Alma Fagyizó", user = MyUser(id = 1)), authentication)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Alma Fagyizó", response.body?.name)
    }

    @Test
    fun testDeletePlaceById() {
        // Arrange
        doNothing().`when`(placeRepository).deleteById(anyLong())
        `when`(placeRepository.findById(anyLong())).thenReturn(
            Optional.of(Place(id = 1, creator = MyUser(id = 1))),
        )
        `when`(placeRepository.existsById(anyLong())).thenReturn((true))

        // Act
        val response = controller.deletePlaceById(1, authentication)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.isSuccess)
    }

    @Test
    fun testDeletePlaceInReviewById() {
        // Arrange
        doNothing().`when`(placeInReviewRepository).deleteById(anyLong())
        `when`(placeInReviewRepository.findById(anyLong())).thenReturn(
            Optional.of(PlaceInReview(id = 1, user = MyUser(id = 1))),
        )
        `when`(placeInReviewRepository.existsById(anyLong())).thenReturn((true))

        // Act
        val response = controller.deletePlaceInReviewById(1, authentication)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.isSuccess)
    }

    @Test
    fun testUpdatePlace() {
        // Arrange
        `when`(placeRepository.findById(anyLong())).thenReturn(Optional.of(Place(name = "Alma Fagyizó", creator = MyUser(id = 1))))
        `when`(placeRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.updatePlace(
            Place(id = 1, name = "Alma Fagyizó2", creator = MyUser(id = 1)),
            authentication,
        )

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Alma Fagyizó2", response.body?.name)
        assertNotEquals("Alma Fagyizó", response.body?.name)
    }
}