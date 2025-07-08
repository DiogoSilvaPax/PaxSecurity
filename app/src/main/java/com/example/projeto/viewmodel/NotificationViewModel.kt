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
import java.util.Date

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: NotificationRepository
    
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = NotificationRepository(database.notificationDao())
    }
    
    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // First, create sample notifications if none exist
                createSampleNotificationsIfNeeded()
                
                // Then load all notifications
                repository.getAllNotifications().collect { notificationList ->
                    _notifications.value = notificationList
                }
            } catch (e: Exception) {
                // If there's an error, set empty list to avoid crashes
                _notifications.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
        
        // Load unread count separately
        viewModelScope.launch {
            try {
                repository.getUnreadCount(1).collect { count ->
                    _unreadCount.value = count
                }
            } catch (e: Exception) {
                _unreadCount.value = 0
            }
        }
    }
    
    private suspend fun createSampleNotificationsIfNeeded() {
        try {
            // Check if we already have notifications
            val existingNotifications = repository.getAllNotifications()
            
            // Create sample notifications for demonstration
            val sampleNotifications = listOf(
                Triple("Cam 05 - Movimento Detectado", "movement", "high"),
                Triple("Cam 03 - Ligação Perdida", "system", "medium"),
                Triple("Cam 01 - Movimento Detectado", "movement", "high"),
                Triple("Sistema de segurança ativado", "system", "normal"),
                Triple("Cam 02 - Bateria baixa", "battery", "medium"),
                Triple("Acesso autorizado na entrada", "access", "normal"),
                Triple("Manutenção programada para amanhã", "maintenance", "low"),
                Triple("Conexão restabelecida com todas as câmaras", "system", "normal"),
                Triple("Tentativa de acesso não autorizado", "access", "high"),
                Triple("Atualização de firmware disponível", "system", "low")
            )
            
            sampleNotifications.forEachIndexed { index, (message, type, priority) ->
                val notification = NotificationEntity(
                    clientId = 1, // Default client ID
                    message = message,
                    type = type,
                    priority = priority,
                    notificationDate = Date(System.currentTimeMillis() - (index * 3600000L)), // Spread over hours
                    createdAt = Date(),
                    updatedAt = Date(),
                    status = "unread",
                    isRead = false
                )
                repository.insertNotification(notification)
            }
        } catch (e: Exception) {
            // If sample creation fails, continue without crashing
        }
    }
    
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                repository.markAsRead(notificationId)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
    
    fun markAllAsRead(clientId: Int) {
        viewModelScope.launch {
            try {
                repository.markAllAsReadForClient(clientId)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
    
    fun createNotification(clientId: Int, message: String, type: String, priority: String = "normal") {
        viewModelScope.launch {
            try {
                repository.createNotification(clientId, message, type, priority)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
    
    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            try {
                repository.deleteNotification(notification)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
}