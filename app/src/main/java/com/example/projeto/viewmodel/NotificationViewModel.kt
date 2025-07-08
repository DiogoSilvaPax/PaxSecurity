package com.example.projeto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.database.AppDatabase
import com.example.projeto.database.entities.NotificationEntity
import com.example.projeto.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: NotificationRepository
    
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    private val _initializedUsers = MutableStateFlow<Set<Int>>(emptySet())
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = NotificationRepository(database.notificationDao())
        loadNotifications()
    }
    
    private fun loadNotifications() {
        viewModelScope.launch {
            repository.getAllNotifications().collect { notificationList ->
                _notifications.value = notificationList.sortedByDescending { it.notificationDate }
            }
        }
    }
    
    fun initializeNotificationsForUser(userId: Int, username: String) {
        viewModelScope.launch {
            // Check if we already initialized notifications for this user
            if (_initializedUsers.value.contains(userId)) {
                return@launch
            }
            
            // Check if user already has notifications
            val existingNotifications = repository.getAllNotifications()
            var hasNotifications = false
            existingNotifications.collect { notifications ->
                hasNotifications = notifications.any { it.clientId == userId }
                if (!hasNotifications) {
                    createNotificationsForUser(userId, username)
                }
            }
            
            // Mark user as initialized
            _initializedUsers.value = _initializedUsers.value + userId
        }
    }
    
    private suspend fun createNotificationsForUser(userId: Int, username: String) {
        val calendar = Calendar.getInstance()
        
        val notificationsData = when (username) {
            "OsmarG" -> listOf(
                Triple("Cam 05 - Movimento Detectado", "movement", 0), // Today
                Triple("Cam 03 - Ligação Perdida", "connection", 4), // 4 days ago
                Triple("Cam 01 - Movimento Detectado", "movement", 7), // 7 days ago
                Triple("Sistema de segurança ativado", "system", 10), // 10 days ago
                Triple("Cam 02 - Bateria baixa", "battery", 12) // 12 days ago
            )
            "DiogoS" -> listOf(
                Triple("Cam 04 - Movimento Detectado", "movement", 0), // Today
                Triple("Cam 06 - Ligação Perdida", "connection", 2), // 2 days ago
                Triple("Cam 02 - Movimento Detectado", "movement", 5), // 5 days ago
                Triple("Cam 01 - Bateria baixa", "battery", 8), // 8 days ago
                Triple("Sistema atualizado", "system", 11) // 11 days ago
            )
            else -> listOf(
                Triple("Cam 01 - Movimento Detectado", "movement", 0),
                Triple("Sistema online", "system", 1),
                Triple("Cam 02 - Bateria baixa", "battery", 3)
            )
        }
        
        notificationsData.forEach { (message, type, daysAgo) ->
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
            
            // Set random time for more realistic notifications
            calendar.set(Calendar.HOUR_OF_DAY, (9..18).random())
            calendar.set(Calendar.MINUTE, (0..59).random())
            
            val notification = NotificationEntity(
                clientId = userId,
                message = message,
                type = type,
                priority = if (type == "movement") "high" else "normal",
                notificationDate = calendar.time,
                createdAt = calendar.time,
                updatedAt = calendar.time,
                status = "unread",
                isRead = false
            )
            repository.insertNotification(notification)
        }
    }
    
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }
    
    fun markAllAsRead(clientId: Int) {
        viewModelScope.launch {
            repository.markAllAsReadForClient(clientId)
        }
    }
    
    fun createNotification(clientId: Int, message: String, type: String, priority: String = "normal") {
        viewModelScope.launch {
            repository.createNotification(clientId, message, type, priority)
        }
    }
    
    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.deleteNotification(notification)
        }
    }
}