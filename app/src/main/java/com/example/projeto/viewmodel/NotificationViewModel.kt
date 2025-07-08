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
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Calendar

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: NotificationRepository
    
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var hasCreatedSampleNotifications = false
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = NotificationRepository(database.notificationDao())
    }
    
    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Show loading for 2 seconds for better UX
                delay(2000)
                
                // Create sample notifications only once
                if (!hasCreatedSampleNotifications) {
                    createSampleNotifications()
                    hasCreatedSampleNotifications = true
                }
                
                // Load all notifications
                repository.getAllNotifications().collect { notificationList ->
                    _notifications.value = notificationList.sortedByDescending { it.notificationDate }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _notifications.value = emptyList()
                _isLoading.value = false
            }
        }
        
        // Load unread count
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
    
    private suspend fun createSampleNotifications() {
        try {
            // Check if notifications already exist
            val existingNotifications = repository.getAllNotifications()
            var hasNotifications = false
            
            existingNotifications.collect { list ->
                hasNotifications = list.isNotEmpty()
            }
            
            if (hasNotifications) return
            
            // Create notifications with different times
            val calendar = Calendar.getInstance()
            
            val sampleNotifications = listOf(
                Triple("Cam 05 - Movimento Detectado", "movement", "high"),
                Triple("Cam 03 - Ligação Perdida", "system", "medium"), 
                Triple("Cam 01 - Movimento Detectado", "movement", "high"),
                Triple("Sistema de segurança ativado", "system", "normal"),
                Triple("Cam 02 - Bateria baixa", "battery", "medium"),
                Triple("Acesso autorizado na entrada", "access", "normal"),
                Triple("Manutenção programada para amanhã", "maintenance", "low"),
                Triple("Conexão restabelecida", "system", "normal"),
                Triple("Tentativa de acesso negado", "access", "high"),
                Triple("Backup concluído", "system", "low")
            )
            
            sampleNotifications.forEachIndexed { index, (message, type, priority) ->
                // Create notifications with different timestamps
                calendar.add(Calendar.HOUR, -index)
                
                val notification = NotificationEntity(
                    clientId = 1,
                    message = message,
                    type = type,
                    priority = priority,
                    notificationDate = calendar.time,
                    createdAt = Date(),
                    updatedAt = Date(),
                    status = "unread",
                    isRead = false
                )
                
                repository.insertNotification(notification)
            }
        } catch (e: Exception) {
            // Continue without crashing
        }
    }
    
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                repository.markAsRead(notificationId)
            } catch (e: Exception) {
                // Handle silently
            }
        }
    }
    
    fun markAllAsRead(clientId: Int) {
        viewModelScope.launch {
            try {
                repository.markAllAsReadForClient(clientId)
            } catch (e: Exception) {
                // Handle silently
            }
        }
    }
    
    fun createNotification(clientId: Int, message: String, type: String, priority: String = "normal") {
        viewModelScope.launch {
            try {
                repository.createNotification(clientId, message, type, priority)
            } catch (e: Exception) {
                // Handle silently
            }
        }
    }
    
    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            try {
                repository.deleteNotification(notification)
            } catch (e: Exception) {
                // Handle silently
            }
        }
    }
}