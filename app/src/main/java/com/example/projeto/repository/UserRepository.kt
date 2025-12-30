package com.example.projeto.repository

import com.example.projeto.database.dao.UserDao
import com.example.projeto.database.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
    private val authDataSource: AuthDataSource
) {

    constructor(userDao: UserDao) : this(userDao, LocalAuthDataSource(userDao))

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    suspend fun authenticateUser(username: String, password: String): User? {
        return authDataSource.authenticateUser(username, password)
    }

    suspend fun registerUser(username: String, password: String, email: String): User? {
        return authDataSource.registerUser(username, password, email)
    }

    suspend fun getUserById(userId: Int): User? {
        return authDataSource.getUserById(userId)
    }

    suspend fun getUserByUsername(username: String): User? {
        return authDataSource.getUserByUsername(username)
    }

    suspend fun getUserByEmail(email: String): User? {
        return authDataSource.getUserByEmail(email)
    }

    suspend fun insertUser(user: User): Long = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun updateLastLogin(userId: Int) {
        authDataSource.updateLastLogin(userId)
    }

    suspend fun updateEmail(userId: Int, email: String) {
        authDataSource.updateEmail(userId, email)
    }

    suspend fun updatePassword(userId: Int, newPassword: String) {
        authDataSource.updatePassword(userId, newPassword)
    }

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
}