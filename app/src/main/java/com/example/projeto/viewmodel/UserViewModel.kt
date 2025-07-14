package com.example.projeto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.database.AppDatabase
import com.example.projeto.database.entities.User
import com.example.projeto.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: UserRepository
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = UserRepository(database.userDao())
    }
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                if (username.isEmpty() || password.isEmpty()) {
                    _loginState.value = LoginState.Error("Por favor, preencha todos os campos")
                    return@launch
                }
                
                val user = repository.authenticateUser(username, password)
                if (user != null) {
                    _currentUser.value = user
                    repository.updateLastLogin(user.userId)
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error("Username ou palavra-passe incorretos")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Erro ao fazer login: ${e.message}")
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
        _loginState.value = LoginState.Idle
    }
    
    fun setLoggedIn() {
        _loginState.value = LoginState.Success
    }
    
    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            try {
                _currentUser.value?.let { user ->
                    repository.updateEmail(user.userId, newEmail)
                    _currentUser.value = user.copy(email = newEmail)
                }
            } catch (e: Exception) {
            }
        }
    }
    
    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                _currentUser.value?.let { user ->
                    repository.updatePassword(user.userId, newPassword)
                }
            } catch (e: Exception) {
            }
        }
    }
    
    fun registerUser(username: String, password: String, email: String) {
        viewModelScope.launch {
            try {
                repository.registerUser(username, password, email)
            } catch (e: Exception) {
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}