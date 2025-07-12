package com.example.projeto.repository

import com.example.projeto.database.dao.UserDao
import com.example.projeto.database.entities.User
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import java.util.Date

/**
 * REPOSITORY USER - Camada de abstração para acesso aos dados dos utilizadores
 * 
 * Esta classe encapsula toda a lógica de acesso aos dados dos utilizadores,
 * incluindo operações CRUD, autenticação e encriptação de passwords.
 */
class UserRepository(private val userDao: UserDao) {
    
    // ==================== CONSULTAS (READ) ====================
    
    /**
     * Obtém todos os utilizadores do sistema
     * @return Flow com lista de utilizadores
     */
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    
    /**
     * Busca utilizador por ID
     * @param userId ID do utilizador
     * @return Utilizador encontrado ou null
     */
    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)
    
    /**
     * Busca utilizador por nome de utilizador
     * @param username Nome de utilizador
     * @return Utilizador encontrado ou null
     */
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
    
    /**
     * Busca utilizador por email
     * @param email Email do utilizador
     * @return Utilizador encontrado ou null
     */
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    // ==================== AUTENTICAÇÃO ====================
    
    /**
     * Autentica utilizador com credenciais
     * Encripta a password fornecida e compara com a armazenada
     * @param username Nome de utilizador
     * @param password Password em texto simples
     * @return Utilizador se credenciais corretas, null caso contrário
     */
    suspend fun authenticateUser(username: String, password: String): User? {
        val hashedPassword = hashPassword(password)
        return userDao.authenticateUser(username, hashedPassword)
    }
    
    // ==================== INSERÇÃO (CREATE) ====================
    
    /**
     * Insere novo utilizador na base de dados
     * @param user Dados do utilizador
     * @return ID do utilizador criado
     */
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    
    /**
     * Regista novo utilizador com password encriptada
     * @param username Nome de utilizador
     * @param password Password em texto simples
     * @param email Email do utilizador
     * @return ID do utilizador criado
     */
    suspend fun registerUser(username: String, password: String, email: String): Long {
        val hashedPassword = hashPassword(password)
        val user = User(
            username = username,
            passwordHash = hashedPassword,
            email = email,
            createdAt = Date(),
            updatedAt = Date()
        )
        return insertUser(user)
    }
    
    // ==================== ATUALIZAÇÃO (UPDATE) ====================
    
    /**
     * Atualiza dados completos do utilizador
     * @param user Utilizador com dados atualizados
     */
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    /**
     * Atualiza data do último login
     * @param userId ID do utilizador
     */
    suspend fun updateLastLogin(userId: Int) {
        userDao.updateLastLogin(userId, Date())
    }
    
    /**
     * Atualiza email do utilizador
     * @param userId ID do utilizador
     * @param email Novo email
     */
    suspend fun updateEmail(userId: Int, email: String) {
        userDao.updateEmail(userId, email, Date())
    }
    
    /**
     * Atualiza password do utilizador
     * @param userId ID do utilizador
     * @param newPassword Nova password em texto simples
     */
    suspend fun updatePassword(userId: Int, newPassword: String) {
        val hashedPassword = hashPassword(newPassword)
        userDao.updatePassword(userId, hashedPassword, Date())
    }
    
    // ==================== ELIMINAÇÃO (DELETE) ====================
    
    /**
     * Elimina utilizador da base de dados
     * @param user Utilizador a eliminar
     */
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    // ==================== UTILITÁRIOS PRIVADOS ====================
    
    /**
     * Encripta password usando SHA-256
     * @param password Password em texto simples
     * @return Password encriptada em hexadecimal
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}