package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * DAO USER - Interface de acesso aos dados dos utilizadores
 * 
 * Define todas as operações de base de dados relacionadas com utilizadores.
 * Inclui operações CRUD, autenticação e queries específicas.
 */
@Dao
interface UserDao {
    
    // ==================== CONSULTAS (READ) ====================
    
    /**
     * Obtém todos os utilizadores do sistema
     * @return Flow com lista de utilizadores (atualiza automaticamente)
     */
    @Query("SELECT * FROM contas_utilizador")
    fun getAllUsers(): Flow<List<User>>
    
    /**
     * Busca utilizador por ID
     * @param userId ID do utilizador
     * @return Utilizador encontrado ou null
     */
    @Query("SELECT * FROM contas_utilizador WHERE user_id = :userId")
    suspend fun getUserById(userId: Int): User?
    
    /**
     * Busca utilizador por nome de utilizador (para login)
     * @param username Nome de utilizador
     * @return Utilizador encontrado ou null
     */
    @Query("SELECT * FROM contas_utilizador WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    /**
     * Busca utilizador por email
     * @param email Email do utilizador
     * @return Utilizador encontrado ou null
     */
    @Query("SELECT * FROM contas_utilizador WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    /**
     * Autentica utilizador com username e password
     * @param username Nome de utilizador
     * @param passwordHash Password encriptada
     * @return Utilizador se credenciais corretas, null caso contrário
     */
    @Query("SELECT * FROM contas_utilizador WHERE username = :username AND password_hash = :passwordHash")
    suspend fun authenticateUser(username: String, passwordHash: String): User?
    
    // ==================== INSERÇÃO (CREATE) ====================
    
    /**
     * Insere novo utilizador na base de dados
     * @param user Dados do utilizador
     * @return ID do utilizador criado
     */
    @Insert
    suspend fun insertUser(user: User): Long
    
    // ==================== ATUALIZAÇÃO (UPDATE) ====================
    
    /**
     * Atualiza dados completos do utilizador
     * @param user Utilizador com dados atualizados
     */
    @Update
    suspend fun updateUser(user: User)
    
    /**
     * Atualiza apenas a data do último login
     * @param userId ID do utilizador
     * @param lastLogin Nova data de login
     */
    @Query("UPDATE contas_utilizador SET last_login = :lastLogin WHERE user_id = :userId")
    suspend fun updateLastLogin(userId: Int, lastLogin: Date)
    
    /**
     * Atualiza apenas o email do utilizador
     * @param userId ID do utilizador
     * @param email Novo email
     * @param updatedAt Data da atualização
     */
    @Query("UPDATE contas_utilizador SET email = :email, updated_at = :updatedAt WHERE user_id = :userId")
    suspend fun updateEmail(userId: Int, email: String, updatedAt: Date)
    
    /**
     * Atualiza apenas a password do utilizador
     * @param userId ID do utilizador
     * @param passwordHash Nova password encriptada
     * @param updatedAt Data da atualização
     */
    @Query("UPDATE contas_utilizador SET password_hash = :passwordHash, updated_at = :updatedAt WHERE user_id = :userId")
    suspend fun updatePassword(userId: Int, passwordHash: String, updatedAt: Date)
    
    // ==================== ELIMINAÇÃO (DELETE) ====================
    
    /**
     * Elimina utilizador da base de dados
     * @param user Utilizador a eliminar
     */
    @Delete
    suspend fun deleteUser(user: User)
}