package id.anantyan.foodapps.domain.repository

import id.anantyan.foodapps.common.R
import id.anantyan.foodapps.common.UIState
import id.anantyan.foodapps.domain.model.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var userUseCase: UserUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userUseCase = UserUseCase(userRepository)
    }

    @After
    fun down() {
        MockitoAnnotations.openMocks(this).close()
    }

    @Test
    fun `login success`() = runTest {
        val testData = UserModel(id = 1, username = "aryarezza", email = "aryarezza@email.com", password = "Rahasia123")
        `when`(userRepository.login(testData)).thenReturn(testData)

        val state = userUseCase.executeLogin(testData)

        assertEquals(testData, state)
    }

    @Test
    fun `login failed`() = runTest {
        val testData = UserModel()
        `when`(userRepository.login(testData)).thenReturn(null)

        val state = userUseCase.executeLogin(testData)

        assertEquals(null, state)
    }

    @Test
    fun `register loading`() = runTest {
        val testData = UserModel(id = 1, username = "aryarezza", email = "aryarezza@email.com", password = "Rahasia123")
        `when`(userRepository.register(testData)).thenReturn(flowOf(UIState.Loading()))

        val state = userUseCase.executeRegister(testData)

        assertTrue(state.first() is UIState.Loading)
    }

    @Test
    fun `register success`() = runTest {
        val testData = UserModel(id = 1, username = "aryarezza", email = "aryarezza@email.com", password = "Rahasia123")
        `when`(userRepository.register(testData)).thenReturn(flowOf(UIState.Success(1)))

        val state = userUseCase.executeRegister(testData)

        assertEquals(1, state.first().data)
    }

    @Test
    fun `register failed`() = runTest {
        val testData = R.string.txt_invalid_register
        val request = UserModel()
        `when`(userRepository.register(request)).thenReturn(flowOf(UIState.Error(null, testData)))

        val state = userUseCase.executeRegister(request)

        assertEquals(testData, state.first().message)
    }

    @Test
    fun `profile loading`() = runTest {
        val request = 1
        `when`(userRepository.profile(request)).thenReturn(flowOf(UIState.Loading()))

        val state = userUseCase.executeProfile(request)

        assertTrue(state.first() is UIState.Loading)
    }

    @Test
    fun `profile success`() = runTest {
        val request = 1
        val testData = UserModel(id = 1, username = "aryarezza", email = "aryarezza@email.com", password = "Rahasia123")
        `when`(userRepository.profile(request)).thenReturn(flowOf(UIState.Success(testData)))

        val state = userUseCase.executeProfile(request)

        assertEquals(testData, state.first().data)
    }

    @Test
    fun `profile failed`() = runTest {
        val request = null
        val testData = R.string.txt_invalid_profile
        `when`(userRepository.profile(request)).thenReturn(flowOf(UIState.Error(null, testData)))

        val state = userUseCase.executeProfile(request)

        assertEquals(testData, state.first().message)
    }

    @Test
    fun `change profile test`() = runTest {
        val testData = UserModel(id = 1, username = "aryarezza", email = "aryarezza@email.com", password = "Rahasia123")

        userUseCase.executeChangeProfile(testData)

        verify(userRepository).changeProfile(testData)
    }
}