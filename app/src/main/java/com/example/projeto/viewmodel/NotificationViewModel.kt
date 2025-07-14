package com.example.projeto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.database.AppDatabase
import com.example.projeto.database.entities.NotificationEntity
import com.example.projeto.repository.NotificationRepository
import com.example.projeto.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Calendar

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val notificationRepository: NotificationRepository
    private val userRepository: UserRepository
    
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var currentUserId: Int = 1
    
    init {
        val database = AppDatabase.getDatabase(application)
        notificationRepository = NotificationRepository(database.notificationDao())
        userRepository = UserRepository(database.userDao())
    }
    
    fun loadNotificationsForUser(userId: Int? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                userId?.let { currentUserId = it }
                
                delay(2000)
                
                createUserSpecificNotifications(currentUserId)
                
                notificationRepository.getNotificationsByClientId(currentUserId).collect { notificationList ->
                    _notifications.value = notificationList.sortedByDescending { it.notificationDate }
                    _isLoading.value = false
                }
                
            } catch (e: Exception) {
                _notifications.value = emptyList()
                _isLoading.value = false
            }
        }
        
        viewModelScope.launch {
            try {
                notificationRepository.getUnreadCount(currentUserId).collect { count ->
                    _unreadCount.value = count
                }
            } catch (e: Exception) {
                _unreadCount.value = 0
            }
        }
    }
    
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                notificationRepository.markAsRead(notificationId)
            } catch (e: Exception) {
            }
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                notificationRepository.markAllAsReadForClient(currentUserId)
            } catch (e: Exception) {
            }
        }
    }
    
    private suspend fun createUserSpecificNotifications(userId: Int) {
        try {
            val existingNotifications = notificationRepository.getNotificationsByClientId(userId)
            var hasNotifications = false
            
            existingNotifications.collect { list ->
                hasNotifications = list.isNotEmpty()
            }
            
            if (hasNotifications) return
            
            val user = userRepository.getUserById(userId)
            val username = user?.username ?: "User$userId"
            
            val userNotifications = getUserSpecificNotifications(username)
            
            val calendar = Calendar.getInstance()
            
            userNotifications.forEachIndexed { index, (message, type, priority) ->
                calendar.add(Calendar.HOUR, -index)
                
                val notification = NotificationEntity(
                    clientId = userId,
                    message = message,
                    type = type,
                    priority = priority,
                    notificationDate = calendar.time,
                    createdAt = Date(),
                    updatedAt = Date(),
                    status = "unread",
                    isRead = false
                )
                
                notificationRepository.insertNotification(notification)
            }
            
        } catch (e: Exception) {
        }
    }
    
    private fun getUserSpecificNotifications(username: String): List<Triple<String, String, String>> {
        return when (username.lowercase()) {
            "osmarg" -> listOf(
                Triple("🏠 Cam 05 - Movimento detectado no quintal", "movement", "high"),
                Triple("⚠️ Cam 03 - Ligação perdida na sala", "system", "medium"),
                Triple("🚨 Cam 01 - Movimento suspeito na entrada", "movement", "high"),
                Triple("✅ Sistema de segurança ativado automaticamente", "system", "normal"),
                Triple("🔋 Cam 02 - Bateria baixa no quarto (15%)", "battery", "medium"),
                Triple("🔓 Acesso autorizado - Porta principal", "access", "normal"),
                Triple("🔧 Manutenção programada para amanhã às 14h", "maintenance", "low"),
                Triple("🌐 Todas as câmaras online e funcionais", "system", "normal"),
                Triple("🚪 Tentativa de acesso negado - Porta traseira", "access", "high"),
                Triple("💾 Backup automático concluído com sucesso", "system", "low")
            )
            
            "diogos" -> listOf(
                Triple("🏢 Cam 04 - Movimento no estacionamento", "movement", "high"),
                Triple("📡 Cam 06 - Conexão instável na receção", "system", "medium"),
                Triple("👥 Cam 02 - Múltiplas pessoas detectadas", "movement", "high"),
                Triple("🔒 Sistema de alarme ativado - Modo noturno", "system", "normal"),
                Triple("⚡ Cam 01 - Bateria crítica (5%) - Substituir", "battery", "high"),
                Triple("🚫 Acesso negado - Cartão não reconhecido", "access", "high"),
                Triple("⚙️ Atualização de firmware disponível", "maintenance", "normal"),
                Triple("📊 Relatório semanal de atividade gerado", "system", "normal"),
                Triple("🔍 Análise de movimento - Padrão anómalo", "movement", "medium"),
                Triple("☁️ Sincronização com cloud concluída", "system", "low")
            )
            
            "admin" -> listOf(
                Triple("👨‍💼 Novo utilizador registado no sistema", "system", "normal"),
                Triple("📈 Relatório de performance - Sistema estável", "system", "low"),
                Triple("🔧 Manutenção de servidor agendada", "maintenance", "medium"),
                Triple("⚠️ Tentativa de login falhada - IP suspeito", "security", "high"),
                Triple("💿 Backup completo do sistema realizado", "system", "normal"),
                Triple("🔄 Atualização de segurança instalada", "system", "medium"),
                Triple("📊 Estatísticas mensais disponíveis", "system", "low"),
                Triple("🛡️ Firewall bloqueou 15 tentativas de acesso", "security", "medium")
            )
            
            else -> listOf(
                Triple("📱 Bem-vindo ao sistema de segurança", "system", "normal"),
                Triple("🔔 Configure as suas preferências", "system", "low"),
                Triple("📋 Consulte o manual do utilizador", "system", "low"),
                Triple("🎯 Sistema configurado com sucesso", "system", "normal"),
                Triple("🔐 Altere a sua palavra-passe regularmente", "security", "medium"),
                Triple("📞 Contacte o suporte para assistência", "system", "low")
            )
        }
    }
}