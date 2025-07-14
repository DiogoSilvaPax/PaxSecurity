package com.example.projeto.database

import android.content.Context
import com.example.projeto.database.entities.User
import com.example.projeto.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Date

class DatabaseInitializer(private val context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val userRepository = UserRepository(database.userDao())
    
    fun initializeDefaultUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val defaultUsers = listOf(
                    Triple("OsmarG", "osmar123", "osmar@security.com"),
                    Triple("DiogoS", "diogo123", "diogo@security.com"),
                    Triple("admin", "admin123", "admin@security.com"),
                )
                
                defaultUsers.forEach { (username, password, email) ->
                    val existingUser = userRepository.getUserByUsername(username)
                    
                    if (existingUser == null) {
                        val userId = createUser(username, password, email)
                    }
                }
                
            } catch (e: Exception) {
            }
        }
    }
    
    private suspend fun createUser(username: String, password: String, email: String): Long {
        val hashedPassword = hashPassword(password)
        
        val role = when (username) {
            "admin" -> "admin"
            "OsmarG", "DiogoS" -> "manager"
            else -> "user"
        }
        
        val user = User(
            username = username,
            passwordHash = hashedPassword,
            email = email,
            role = role,
            createdAt = Date(),
            updatedAt = Date(),
            status = "active"
        )
        
        return userRepository.insertUser(user)
    }
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}