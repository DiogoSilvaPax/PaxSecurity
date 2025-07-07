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

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: NotificationRepository
    
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = NotificationRepository(database.notificationDao())
        loadNotifications()
        loadSampleNotifications()
    }
    
    private fun loadNotifications() {
        viewModelScope.launch {
            repository.getAllNotifications().collect { notificationList ->
                _notifications.value = notificationList
            }
        }
        
        viewModelScope.launch {
            // Assuming client ID 1 for demo purposes
            repository.getUnreadCount(1).collect { count ->
                _unreadCount.value = count
            }
        }
    }
    
    private fun loadSampleNotifications() {
        viewModelScope.launch {
            // Create some sample notifications if database is empty
            val sampleNotifications = listOf(
                Triple("Movimento Detectado", "Câmara 01 - Entrada principal", "movement"),
                Triple("Sistema Online", "Todas as câmaras conectadas", "system"),
                Triple("Bateria Baixa", "Câmara 03 - Jardim", "battery"),
                Triple("Acesso Negado", "Tentativa de login falhada", "access"),
                Triple("Manutenção", "Sistema será atualizado às 02:00", "maintenance"),
                Triple("Movimento Detectado", "Câmara 05 - Garagem", "movement")
            )
            
            sampleNotifications.forEach { (title, message, type) ->
                createNotification(1, message, type)
            }
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