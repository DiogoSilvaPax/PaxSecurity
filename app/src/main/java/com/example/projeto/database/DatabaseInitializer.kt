package com.example.projeto.database

import android.content.Context
import com.example.projeto.database.entities.User
import com.example.projeto.database.entities.NotificationEntity
import com.example.projeto.repository.UserRepository
import com.example.projeto.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Date

class DatabaseInitializer(private val context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val userRepository = UserRepository(database.userDao())
    private val notificationRepository = NotificationRepository(database.notificationDao())
    
    fun initializeDefaultUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Check if users already exist
                val existingUser1 = userRepository.getUserByUsername("OsmarG")
                val existingUser2 = userRepository.getUserByUsername("DiogoS")
                
                var user1Id: Long? = null
                var user2Id: Long? = null
                
                if (existingUser1 == null) {
                    user1Id = createUser("OsmarG", "osmar123", "osmar@example.com")
                } else {
                    user1Id = existingUser1.userId.toLong()
                }
                
                if (existingUser2 == null) {
                    user2Id = createUser("DiogoS", "diogo123", "diogo@example.com")
                } else {
                    user2Id = existingUser2.userId.toLong()
                }
                
                // Create notifications for each user
                user1Id?.let { createNotificationsForUser(it.toInt()) }
                user2Id?.let { createNotificationsForUser(it.toInt()) }
                
            } catch (e: Exception) {
                // Handle initialization errors gracefully
            }
        }
    }
    
    private suspend fun createUser(username: String, password: String, email: String): Long {
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
        return userRepository.insertUser(user)
    }
    
    private suspend fun createNotificationsForUser(userId: Int) {
        try {
            val userNotifications = listOf(
                Triple("Cam 05 - Movimento Detectado", "movement", "high"),
                Triple("Cam 03 - Ligação Perdida", "system", "medium"),
                Triple("Cam 01 - Movimento Detectado", "movement", "high"),
                Triple("Sistema de segurança ativado", "system", "normal"),
                Triple("Cam 02 - Bateria baixa", "battery", "medium"),
                Triple("Acesso autorizado na entrada", "access", "normal"),
                Triple("Manutenção programada", "maintenance", "low"),
                Triple("Todas as câmaras online", "system", "normal")
            )
            
            userNotifications.forEachIndexed { index, (message, type, priority) ->
                val notification = NotificationEntity(
                    clientId = userId,
                    message = message,
                    type = type,
                    priority = priority,
                    notificationDate = Date(System.currentTimeMillis() - (index * 3600000L)),
                    createdAt = Date(),
                    updatedAt = Date(),
                    status = "unread",
                    isRead = false
                )
                notificationRepository.insertNotification(notification)
            }
        } catch (e: Exception) {
            // Handle notification creation errors
        }
    }
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}