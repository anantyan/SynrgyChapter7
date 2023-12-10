package id.anantyan.foodapps.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.anantyan.foodapps.common.MainDispatcherRule
import id.anantyan.foodapps.common.R
import id.anantyan.foodapps.common.UIState
import id.anantyan.foodapps.domain.model.UserModel
import id.anantyan.foodapps.domain.repository.PreferencesUseCase
import id.anantyan.foodapps.domain.repository.UserUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class LoginViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule: MainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var preferencesUseCase: PreferencesUseCase

    @Mock
    private lateinit var userUseCase: UserUseCase

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(preferencesUseCase, userUseCase)
    }

    @After
    fun down() {
        MockitoAnnotations.openMocks(this).close()
    }

    @Test
    fun `login success`() = runTest {
        val testData = UserModel(
            id = 1,
            username = "aryarezza",
            email = "aryarezza@email.com",
            password = "Rahasia123"
        )
        `when`(userUseCase.executeLogin(testData)).thenReturn(testData)

        viewModel.login(testData)

        val state = viewModel.login.first()

        assertTrue(state is UIState.Success)
        assertEquals(testData, state?.data)
    }

    @Test
    fun `login failed`() = runTest {
        val testData = R.string.txt_invalid_login
        val request = UserModel()
        `when`(userUseCase.executeLogin(request)).thenReturn(null)

        viewModel.login(request)

        val state = viewModel.login.first()

        assertTrue(state is UIState.Error)
        assertEquals(testData, state?.message)
    }

    @Test
    fun `theme true`() = runTest {
        val testData = true
        `when`(preferencesUseCase.executeGetTheme()).thenReturn(flowOf(testData))

        val state = viewModel.getTheme().first()

        assertEquals(testData, state)
    }

    @Test
    fun `theme false`() = runTest {
        val testData = false
        `when`(preferencesUseCase.executeGetTheme()).thenReturn(flowOf(testData))

        val state = viewModel.getTheme().first()

        assertEquals(testData, state)
    }

    @Test
    fun `setTheme value`() = runTest {
        val testData = true

        viewModel.setTheme(testData)

        verify(preferencesUseCase).executeSetTheme(testData)
    }
}
