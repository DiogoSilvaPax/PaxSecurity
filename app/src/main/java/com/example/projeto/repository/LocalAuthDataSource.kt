package com.example.projeto.repository

import com.example.projeto.database.dao.UserDao
import com.example.projeto.database.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.Date

class LocalAuthDataSource(private val userDao: UserDao) : AuthDataSource {

    override suspend fun authenticateUser(username: String, password: String): User? = withContext(Dispatchers.IO) {
        val hashedPassword = hashPassword(password)
        userDao.authenticateUser(username, hashedPassword)
    }

    override suspend fun registerUser(username: String, password: String, email: String): User? = withContext(Dispatchers.IO) {
        val hashedPassword = hashPassword(password)
        val user = User(
            username = username,
            passwordHash = hashedPassword,
            email = email,
            createdAt = Date(),
            updatedAt = Date()
        )
        val userId = userDao.insertUser(user)
        user.copy(userId = userId.toInt())
    }

    override suspend fun getUserById(userId: Int): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(userId)
    }

    override suspend fun getUserByUsername(username: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByUsername(username)
    }

    override suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)
    }

    override suspend fun updateLastLogin(userId: Int) = withContext(Dispatchers.IO) {
        userDao.updateLastLogin(userId, Date())
    }

    override suspend fun updateEmail(userId: Int, email: String) = withContext(Dispatchers.IO) {
        userDao.updateEmail(userId, email, Date())
    }

    override suspend fun updatePassword(userId: Int, newPassword: String) = withContext(Dispatchers.IO) {
        val hashedPassword = hashPassword(newPassword)
        userDao.updatePassword(userId, hashedPassword, Date())
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}
