package com.example.loginscreen.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginscreen.Event.UserEvent
import com.example.loginscreen.TableUsers.User
import com.example.loginscreen.TableUsers.UserDao.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDao) : ViewModel() {

    val _state = MutableStateFlow(UserState())



    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SetfullName -> {
                _state.update {
                    it.copy(
                        fullName = event.fullName
                    )
                }
            }

            is UserEvent.SetEmail -> {
                _state.update {
                    it.copy(
                        fullName = event.Email
                    )
                }
            }

            is UserEvent.SetloginId -> {
                _state.update {
                    it.copy(
                        fullName = event.loginId
                    )
                }
            }

            is UserEvent.Setpassword -> {
                _state.update {
                    it.copy(
                        fullName = event.password
                    )
                }
            }

            is UserEvent.SetCorrectpassword -> {
                _state.update {
                    it.copy(
                        fullName = event.Correctpassword
                    )
                }
            }

            is UserEvent.SetphoneNumber -> {
                _state.update {
                    it.copy(
                        fullName = event.phoneNumber
                    )
                }
            }

            UserEvent.SaveContact -> {
                val fullName = _state.value.fullName
                val loginId = _state.value.loginId
                val phoneNumber = _state.value.phoneNumber
                val email = _state.value.email
                val password = _state.value.password

                if(fullName.isBlank() || loginId.isBlank() || phoneNumber.isBlank() || email.isBlank()){
                    return
                }
                val user = User(
                    fullName = fullName,
                    loginId = loginId,
                    phoneNumber = phoneNumber,
                    Email = email,
                    password = password
                )

                viewModelScope.launch {
                    dao.upsertUser(user = user)
                }

                _state.update { it.copy(
                    fullName = "",
                    loginId = "",
                    phoneNumber = "",
                    password = "",
                    email = ""
                ) }

            }


        }
    }

}