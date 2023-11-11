package hu.zsof.restaurantApp

import hu.zsof.restaurantApp.model.MyUser
import hu.zsof.restaurantApp.model.request.LoginData
import hu.zsof.restaurantApp.model.response.VerificationResponse
import hu.zsof.restaurantApp.repository.PlaceRepository
import hu.zsof.restaurantApp.repository.UserRepository
import hu.zsof.restaurantApp.security.SecurityController
import hu.zsof.restaurantApp.security.SecurityService
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
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
class SecurityControllerTests {

    private lateinit var controller: SecurityController

    private lateinit var authentication: JwtAuthenticationToken

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var placeRepository: PlaceRepository

    @Mock
    private lateinit var mailService: MailService

    @Mock
    private lateinit var securityService: SecurityService

    @BeforeEach
    fun setUp() {
        val userService = UserService(
            userRepository = userRepository,
            placeRepository = placeRepository,
            mailService = mailService,
        )

        controller = SecurityController(
            userService = userService,
            securityService = securityService,
        )

        authentication = JwtAuthenticationToken(
            mock(Jwt::class.java),
            listOf(mock(SimpleGrantedAuthority::class.java)),
            "user@test.hu",
        )
    }

    @Test
    fun testAuthorize() {
        // Arrange
        `when`(securityService.verifyToken("1")).thenReturn(
            VerificationResponse(
                verified = true,
                isAdmin = false,
                errorMessage = "",
                userId = 1,
            ),
        )
        `when`(userRepository.findById(anyLong())).thenReturn(Optional.of(MyUser(id = 1, name = "Alma")))

        // Act
        val response = controller.authorize("1")

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.isSuccess)
    }

    @Test
    fun testLogin() {
        // Arrange
        `when`(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(MyUser(id = 1, name = "Alma")))

        // Act
        val response = controller.login(authentication, mock(HttpServletResponse::class.java))

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.isSuccess)
        assertEquals("Belépés sikeres!", response.body?.successMessage)
    }

    @Test
    fun testRegister() {
        // Arrange
        val loginData = LoginData(email = "test@test.hu", password = "Alma1234", name = "TestUser")
        `when`(userRepository.save(any())).thenAnswer { i: InvocationOnMock -> i.arguments[0] }

        // Act
        val response = controller.register(loginData, isAdmin = false, isOwner = false)

        // Assert
        assertNotNull(response.body)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(true, response.body?.isSuccess)
        assertEquals("Regisztráció sikeres! Kérlek aktiváld fiókodat.", response.body?.successMessage)
    }
}