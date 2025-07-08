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
            // Check if users already exist
            val existingUser1 = userRepository.getUserByUsername("OsmarG")
            val existingUser2 = userRepository.getUserByUsername("DiogoS")
            
            if (existingUser1 == null) {
                createUser("OsmarG", "osmar123", "osmar@example.com")
            }
            
            if (existingUser2 == null) {
                createUser("DiogoS", "diogo123", "diogo@example.com")
            }
        }
    }
    
    private suspend fun createUser(username: String, password: String, email: String) {
        val hashedPassword = hashPassword(password)
        val user = User(
            username = username,
            passwordHash = hashedPassword,
            email = email,
            role = "admin",
            createdAt = Date(),
            updatedAt = Date(),
            status = "active"
        )
        userRepository.insertUser(user)
    }
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}