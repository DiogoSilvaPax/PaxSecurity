package com.example.projeto.repository

import com.example.projeto.database.entities.User

interface AuthDataSource {
    suspend fun authenticateUser(username: String, password: String): User?
    suspend fun registerUser(username: String, password: String, email: String): User?
    suspend fun getUserById(userId: Int): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateLastLogin(userId: Int)
    suspend fun updateEmail(userId: Int, email: String)
    suspend fun updatePassword(userId: Int, newPassword: String)
}
