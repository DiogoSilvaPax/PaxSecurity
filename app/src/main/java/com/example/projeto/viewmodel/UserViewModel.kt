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

/**
 * VIEWMODEL USER - Gere o estado da UI relacionado com utilizadores
 * 
 * Esta classe é responsável por gerir o estado de autenticação,
 * perfil do utilizador e comunicar com o repository.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {
    
    // ==================== DEPENDÊNCIAS ====================
    
    private val repository: UserRepository
    
    // ==================== ESTADO DA UI ====================
    
    // Utilizador atualmente logado
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    // Estado do processo de login
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    // ==================== INICIALIZAÇÃO ====================
    
    init {
        // Inicializa repository com acesso à base de dados
        val database = AppDatabase.getDatabase(application)
        repository = UserRepository(database.userDao())
    }
    
    // ==================== AUTENTICAÇÃO ====================
    
    /**
     * Executa processo de login
     * @param username Nome de utilizador
     * @param password Password em texto simples
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                // Validação básica dos campos
                if (username.isEmpty() || password.isEmpty()) {
                    _loginState.value = LoginState.Error("Por favor, preencha todos os campos")
                    return@launch
                }
                
                // Tenta autenticar utilizador
                val user = repository.authenticateUser(username, password)
                if (user != null) {
                    // Login bem-sucedido
                    _currentUser.value = user
                    repository.updateLastLogin(user.userId)
                    _loginState.value = LoginState.Success
                } else {
                    // Credenciais incorretas
                    _loginState.value = LoginState.Error("Username ou palavra-passe incorretos")
                }
            } catch (e: Exception) {
                // Erro durante o processo de login
                _loginState.value = LoginState.Error("Erro ao fazer login: ${e.message}")
            }
        }
    }
    
    /**
     * Executa logout do utilizador
     */
    fun logout() {
        _currentUser.value = null
        _loginState.value = LoginState.Idle
    }
    
    /**
     * Define utilizador como logado (para casos especiais)
     */
    fun setLoggedIn() {
        _loginState.value = LoginState.Success
    }
    
    // ==================== GESTÃO DE PERFIL ====================
    
    /**
     * Atualiza email do utilizador atual
     * @param newEmail Novo email
     */
    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            try {
                _currentUser.value?.let { user ->
                    repository.updateEmail(user.userId, newEmail)
                    // Atualiza estado local
                    _currentUser.value = user.copy(email = newEmail)
                }
            } catch (e: Exception) {
                // Tratar erro silenciosamente ou mostrar mensagem
            }
        }
    }
    
    /**
     * Atualiza password do utilizador atual
     * @param newPassword Nova password em texto simples
     */
    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                _currentUser.value?.let { user ->
                    repository.updatePassword(user.userId, newPassword)
                }
            } catch (e: Exception) {
                // Tratar erro silenciosamente ou mostrar mensagem
            }
        }
    }
    
    // ==================== REGISTO DE UTILIZADORES ====================
    
    /**
     * Regista novo utilizador no sistema
     * @param username Nome de utilizador
     * @param password Password em texto simples
     * @param email Email do utilizador
     */
    fun registerUser(username: String, password: String, email: String) {
        viewModelScope.launch {
            try {
                repository.registerUser(username, password, email)
            } catch (e: Exception) {
                // Tratar erro
            }
        }
    }
}

/**
 * ESTADOS DO LOGIN - Representa os diferentes estados do processo de autenticação
 */
sealed class LoginState {
    object Idle : LoginState()        // Estado inicial
    object Loading : LoginState()     // A processar login
    object Success : LoginState()     // Login bem-sucedido
    data class Error(val message: String) : LoginState()  // Erro com mensagem
}