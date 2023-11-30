package id.anantyan.foodapps.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

    private var _login: MutableLiveData<UIState<UserModel>> = MutableLiveData()

    val login: LiveData<UIState<UserModel>> = _login
    val getTheme: Flow<Boolean> = preferencesUseCase.executeGetTheme()

    fun login(user: UserModel) {
        viewModelScope.launch {
            _login.postValue(UIState.Loading())
            val response = userUseCase.executeLogin(user)
            if (response != null) {
                preferencesUseCase.executeSetLogin(true)
                preferencesUseCase.executeSetUsrId(response.id ?: -1)
                _login.postValue(UIState.Success(response))
            } else {
                _login.postValue(UIState.Error(null, R.string.txt_invalid_login))
            }
        }
    }

    fun setTheme(value: Boolean) {
        viewModelScope.launch { preferencesUseCase.executeSetTheme(value) }
    }
}