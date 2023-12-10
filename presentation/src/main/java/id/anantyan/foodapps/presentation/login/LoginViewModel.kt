package id.anantyan.foodapps.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.anantyan.foodapps.common.R
import id.anantyan.foodapps.common.UIState
import id.anantyan.foodapps.domain.model.UserModel
import id.anantyan.foodapps.domain.repository.PreferencesUseCase
import id.anantyan.foodapps.domain.repository.UserUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private var _login: MutableStateFlow<UIState<UserModel>?> = MutableStateFlow(null)

    val login: StateFlow<UIState<UserModel>?> = _login

    fun login(user: UserModel) {
        viewModelScope.launch {
            _login.value = UIState.Loading()
            val response = userUseCase.executeLogin(user)
            if (response != null) {
                preferencesUseCase.executeSetLogin(true)
                preferencesUseCase.executeSetUsrId(response.id ?: -1)
                _login.value = UIState.Success(response)
            } else {
                _login.value = UIState.Error(null, R.string.txt_invalid_login)
            }
        }
    }

    fun setTheme(value: Boolean) {
        viewModelScope.launch {
            preferencesUseCase.executeSetTheme(value)
        }
    }

    fun getTheme(): Flow<Boolean> {
        return preferencesUseCase.executeGetTheme()
    }
}
