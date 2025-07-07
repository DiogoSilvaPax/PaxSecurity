package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface UserDao {
    @Query("SELECT * FROM contas_utilizador")
    fun getAllUsers(): Flow<List<User>>
    
    @Query("SELECT * FROM contas_utilizador WHERE user_id = :userId")
    suspend fun getUserById(userId: Int): User?
    
    @Query("SELECT * FROM contas_utilizador WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    @Query("SELECT * FROM contas_utilizador WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT * FROM contas_utilizador WHERE username = :username AND password_hash = :passwordHash")
    suspend fun authenticateUser(username: String, passwordHash: String): User?
    
    @Insert
    suspend fun insertUser(user: User): Long
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("UPDATE contas_utilizador SET last_login = :lastLogin WHERE user_id = :userId")
    suspend fun updateLastLogin(userId: Int, lastLogin: Date)
    
    @Query("UPDATE contas_utilizador SET email = :email, updated_at = :updatedAt WHERE user_id = :userId")
    suspend fun updateEmail(userId: Int, email: String, updatedAt: Date)
    
    @Query("UPDATE contas_utilizador SET password_hash = :passwordHash, updated_at = :updatedAt WHERE user_id = :userId")
    suspend fun updatePassword(userId: Int, passwordHash: String, updatedAt: Date)
}