package id.anantyan.foodapps.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.anantyan.foodapps.common.R
import id.anantyan.foodapps.common.UIState
import id.anantyan.foodapps.domain.repository.PreferencesUseCase
import id.anantyan.foodapps.domain.repository.UserUseCase
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun showPhoto(): Flow<String> = flow {
        preferencesUseCase.executeGetUserId().flatMapLatest {
            userUseCase.executeProfile(it)
        }.collect { state ->
            emit(state.data?.image ?: "")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun showProfile(): Flow<UIState<List<ProfileItemModel>>> = flow {
        preferencesUseCase.executeGetUserId().flatMapLatest {
            userUseCase.executeProfile(it)
        }.collect { state ->
            val response = when (state) {
                is UIState.Loading -> { UIState.Loading() }
                is UIState.Success -> {
                    UIState.Success(
                        listOf(
                            ProfileItemModel(
                                R.drawable.ic_key_id,
                                R.string.txt_id,
                                state.data?.id.toString()
                            ),
                            ProfileItemModel(
                                R.drawable.ic_shield_person,
                                R.string.txt_username,
                                state.data?.username
                            ),
                            ProfileItemModel(
                                R.drawable.ic_email,
                                R.string.txt_email,
                                state.data?.email
                            )
                        )
                    )
                }
                is UIState.Error -> { UIState.Error(null, state.message!!) }
            }
            emit(response)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            preferencesUseCase.executeSetUsrId(-1)
            preferencesUseCase.executeSetLogin(false)
        }
    }
}
