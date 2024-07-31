package com.monitoringtendepay.data.repository

import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.data.remote.apiservice.AuthService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Create AuthService instance directly
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(AuthService::class.java)

        // Initialize AuthRepositoryImpl with the created AuthService instance
        authRepository = AuthRepositoryImpl(authService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login network error returns error`() = runBlocking {
        val mockResponse = MockResponse()
            .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        mockWebServer.enqueue(mockResponse)

        val result = authRepository.login("userLogin", "testUser", "testPassword").first()

        assert(result is Resource.Error)
        val errorMessage = (result as Resource.Error).message
        assertTrue(errorMessage?.contains("Network error") == true)
    }

    @Test
    fun `login success returns user data`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                    "status": "success",
                    "message": "Login successful",
                    "role": "user",
                    "username": "testUser",
                    "sessionToken": "token123",
                    "salutation": "Mr."
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = authRepository.login("userLogin", "testUser", "testPassword").first()

        assert(result is Resource.Success)
        val user = (result as Resource.Success).data?.getOrNull()
        assertNotNull(user)
        assertEquals("Login successful", user?.message)
        assertEquals("success", user?.status)
        assertEquals("user", user?.role)
        assertEquals("testUser", user?.username)
        assertEquals("token123", user?.sessionToken)
        assertEquals("Mr.", user?.salutation)
    }

    @Test
    fun `login unexpected status returns error`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                    "status": "unknownStatus",
                    "message": "Unexpected status",
                    "role": "user",
                    "username": "testUser",
                    "sessionToken": "token123",
                    "salutation": "Mr."
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = authRepository.login("userLogin", "testUser", "testPassword").first()

        assert(result is Resource.Error)
        val errorMessage = (result as Resource.Error).message
        assertTrue(errorMessage?.contains("Unexpected error") == true)
    }

    // Helper function to assert not null
    private fun assertNotNull(value: Any?) {
        if (value == null) {
            fail("Value should not be null")
        }
    }
}