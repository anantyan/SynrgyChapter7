package id.anantyan.foodapps.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import id.anantyan.foodapps.common.MainDispatcherRule
import id.anantyan.foodapps.common.UIState
import id.anantyan.foodapps.common.getOrAwaitValue
import id.anantyan.foodapps.domain.model.UserModel
import id.anantyan.foodapps.domain.repository.PreferencesUseCase
import id.anantyan.foodapps.domain.repository.UserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule: MainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `login success`() = runTest {
        // setup actual and response
        val actual = UserModel(
            id = 1,
            username = "aryarezza",
            email = "aryarezza@email.com",
            password = "Rahasia123"
        )
        val response = UserModel(
            id = 1,
            username = "aryarezza",
            email = "aryarezza@email.com",
            password = "Rahasia123"
        )

        val liveData = MutableLiveData<UIState<UserModel>>()
        liveData.value = UIState.Success(response)

        viewModel.login(actual)

        // proccess verify expectation
        `when`(viewModel.login).thenReturn(liveData)
        val expected = viewModel.login.getOrAwaitValue()
        verify(viewModel).login

        // test
        assertEquals(expected.data, actual)
    }

    @Test
    fun `login error`() = runTest {
        // setup actual and response
        val request = UserModel(
            id = 1,
            username = "aryarezza",
            email = "aryarezza@email.com",
            password = "Rahasia123"
        )
        val response = -1
        val actual = -1

        val liveData = MutableLiveData<UIState<UserModel>>()
        liveData.value = UIState.Error(null, response)

        viewModel.login(request)

        // proccess verify expectation
        `when`(viewModel.login).thenReturn(liveData)
        val expected = viewModel.login.getOrAwaitValue()
        verify(viewModel).login

        // test
        assertEquals(expected.message, actual)
    }

    @Test
    fun `theme success`() = runTest {
        // setup actual and response
        val actual = true
        val response = true

        val flow = MutableStateFlow(false)
        flow.value = response

        viewModel.setTheme(actual)

        // proccess verify expectation
        `when`(viewModel.getTheme).thenReturn(flow)
        val expected = viewModel.getTheme
        verify(viewModel).getTheme

        // test
        assertEquals(expected.first(), actual)
    }

    @Test
    fun `theme error`() = runTest {
        // setup actual and response
        val actual = true
        val response = false

        val flow = MutableStateFlow(false)
        flow.value = response

        viewModel.setTheme(actual)

        // proccess verify expectation
        `when`(viewModel.getTheme).thenReturn(flow)
        val expected = viewModel.getTheme
        verify(viewModel).getTheme

        // test
        assertNotEquals(expected.first(), actual)
    }
}