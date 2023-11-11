package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.controller.PlaceInReviewController
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
class PlaceByAdminControllerTests {

    private lateinit var controller: PlaceInReviewController

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

        controller = PlaceInReviewController(
            placeInReviewService = placeInReviewService,
        )

        authentication = JwtAuthenticationToken(
            mock(Jwt::class.java),
            listOf(mock(SimpleGrantedAuthority::class.java)),
            "1",
        )
    }

    @Test
    fun testGetAllPlaceInReview() {
        // Arrange
        `when`(placeInReviewRepository.findAll()).thenReturn(
            listOf(
                PlaceInReview(id = 1, name = "Alma Fagyizó"),
                PlaceInReview(id = 2, name = "Körte Étterem"),
            ),
        )

        // Act
        val response = controller.getAllPlace()

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotEquals("Körte Étterem", response.body?.get(0)?.name)
        assertEquals("Körte Étterem", response.body?.get(1)?.name)
    }

    @Test
    fun testGetModifiedPlaces() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó", isModified = true),
                Place(id = 2, name = "Körte Étterem", isModified = true),
            ),
        )

        // Act
        val response = controller.getModifiedPlaces()

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotEquals("Körte Étterem", response.body?.get(0)?.name)
        assertEquals("Körte Étterem", response.body?.get(1)?.name)
    }

    @Test
    fun testAcceptPlaceInReview() {
        // Arrange
        `when`(placeInReviewRepository.findById(anyLong())).thenReturn(Optional.of(PlaceInReview()))
        `when`(placeInReviewRepository.existsById(anyLong())).thenReturn((true))
        `when`(placeRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }
        doNothing().`when`(placeInReviewRepository).deleteById(anyLong())

        // Act
        val response = controller.acceptPlace(
            placeId = 1,
            isModifiedPlace = false,
        )

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun testAcceptPlaceModified() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó", isModified = true),
                Place(id = 2, name = "Körte Étterem", isModified = true),
            ),
        )
        `when`(placeRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.acceptPlace(
            placeId = 1,
            isModifiedPlace = true,
        )

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun testReportPlaceInReview() {
        // Arrange
        `when`(placeInReviewRepository.findById(anyLong())).thenReturn(Optional.of(PlaceInReview()))
        `when`(placeInReviewRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.reportProblemPlace(
            problem = "Probléma",
            placeId = 1,
            isModifiedPlace = false,
        )

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun testReportPlaceModified() {
        // Arrange
        `when`(placeRepository.findAll()).thenReturn(
            listOf(
                Place(id = 1, name = "Alma Fagyizó", isModified = true),
                Place(id = 2, name = "Körte Étterem", isModified = true),
            ),
        )
        `when`(placeRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.reportProblemPlace(
            problem = "Probléma",
            placeId = 1,
            isModifiedPlace = true,
        )

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }
}