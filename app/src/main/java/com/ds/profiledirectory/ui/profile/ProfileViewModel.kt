package com.ds.profiledirectory.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.profiledirectory.data.model.UserList
import com.ds.profiledirectory.repository.ProfileListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileListRepository):
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<UserList>>(UiState.Loading)

    val uiState: StateFlow<UiState<UserList>> = _uiState

    fun fetchUserList(no: Int) {
        viewModelScope.launch {
            repository.getUserList(no)
                .catch {
                    _uiState.value = UiState.Error(it.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}