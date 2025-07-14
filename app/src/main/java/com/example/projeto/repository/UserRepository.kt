package com.example.projeto.repository

import com.example.projeto.database.dao.UserDao
import com.example.projeto.database.entities.User
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import java.util.Date

class UserRepository(private val userDao: UserDao) {
    
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    
    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)
    
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
    
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    suspend fun authenticateUser(username: String, password: String): User? {
        val hashedPassword = hashPassword(password)
        return userDao.authenticateUser(username, hashedPassword)
    }
    
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    
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
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun updateLastLogin(userId: Int) {
        userDao.updateLastLogin(userId, Date())
    }
    
    suspend fun updateEmail(userId: Int, email: String) {
        userDao.updateEmail(userId, email, Date())
    }
    
    suspend fun updatePassword(userId: Int, newPassword: String) {
        val hashedPassword = hashPassword(newPassword)
        userDao.updatePassword(userId, hashedPassword, Date())
    }
    
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}