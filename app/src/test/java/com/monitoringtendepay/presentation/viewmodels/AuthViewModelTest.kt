package com.monitoringtendepay.presentation.viewmodels

import com.monitoringtendepay.core.common.PreferenceManager
import com.monitoringtendepay.core.common.Resource
import com.monitoringtendepay.domain.models.LoginUser
import com.monitoringtendepay.domain.repository.AuthRepository
import com.monitoringtendepay.presentation.states.LoginState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class AuthViewModelTest {

    private val mockAuthRepository: AuthRepository = mockk()
    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(mockAuthRepository, mockPreferenceManager)
    }

    @Test
    fun `login success sets LoginState with data`() = runTest {
        val loginUser = LoginUser(
            status = "success",
            message = "Login successful",
            role = "user",
            username = "testUser",
            salutation = "Mr.",
            sessionToken = "token123"
        )

        coEvery { mockAuthRepository.login(any(), any(), any()) } returns
            flowOf(Resource.Success(Result.success(loginUser)))

        viewModel.login("userLogin", "testUser", "hashedPassword")

        val expectedState = LoginState(
            data = loginUser,
            changePasswordRequired = false
        )

        val state = viewModel.loginState.first()
        assertEquals(expectedState, state)
    }

    @Test
    fun `login error sets LoginState with error message`() = runTest {
        coEvery { mockAuthRepository.login(any(), any(), any()) } returns
            flowOf(Resource.Error("Login failed"))

        viewModel.login("userLogin", "testUser", "hashedPassword")

        val expectedState = LoginState(
            error = "Login failed"
        )

        val state = viewModel.loginState.first()
        assertEquals(expectedState, state)
    }
}