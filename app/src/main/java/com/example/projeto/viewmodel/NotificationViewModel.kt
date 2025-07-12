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

/**
 * 🎯 VIEWMODEL NOTIFICAÇÕES - Gere notificações específicas por utilizador
 * 
 * Esta classe é responsável por:
 * - Carregar notificações do utilizador atual
 * - Criar notificações personalizadas por utilizador
 * - Gerir estado de leitura das notificações
 */
class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    // ==================== DEPENDÊNCIAS ====================
    
    private val notificationRepository: NotificationRepository
    private val userRepository: UserRepository
    
    // ==================== ESTADO DA UI ====================
    
    // Lista de notificações do utilizador atual
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()
    
    // Contador de notificações não lidas
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // ID do utilizador atual
    private var currentUserId: Int = 1 // Default para OsmarG
    
    // ==================== INICIALIZAÇÃO ====================
    
    init {
        val database = AppDatabase.getDatabase(application)
        notificationRepository = NotificationRepository(database.notificationDao())
        userRepository = UserRepository(database.userDao())
    }
    
    // ==================== MÉTODOS PÚBLICOS ====================
    
    /**
     * 📱 Carrega notificações do utilizador específico
     * @param userId ID do utilizador (se null, usa o atual)
     */
    fun loadNotificationsForUser(userId: Int? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Define o utilizador atual
                userId?.let { currentUserId = it }
                
                // Mostra loading por 2 segundos (UX)
                delay(2000)
                
                // Cria notificações se não existirem para este utilizador
                createUserSpecificNotifications(currentUserId)
                
                // Carrega notificações da base de dados
                notificationRepository.getNotificationsByClientId(currentUserId).collect { notificationList ->
                    _notifications.value = notificationList.sortedByDescending { it.notificationDate }
                    _isLoading.value = false
                }
                
            } catch (e: Exception) {
                _notifications.value = emptyList()
                _isLoading.value = false
            }
        }
        
        // Carrega contador de não lidas
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
    
    /**
     * ✅ Marca notificação como lida
     */
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                notificationRepository.markAsRead(notificationId)
            } catch (e: Exception) {
                // Tratamento silencioso
            }
        }
    }
    
    /**
     * ✅ Marca todas as notificações como lidas
     */
    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                notificationRepository.markAllAsReadForClient(currentUserId)
            } catch (e: Exception) {
                // Tratamento silencioso
            }
        }
    }
    
    // ==================== MÉTODOS PRIVADOS ====================
    
    /**
     * 🎨 Cria notificações específicas para cada utilizador
     * Cada utilizador tem notificações personalizadas baseadas no seu perfil
     */
    private suspend fun createUserSpecificNotifications(userId: Int) {
        try {
            // Verifica se já existem notificações para este utilizador
            val existingNotifications = notificationRepository.getNotificationsByClientId(userId)
            var hasNotifications = false
            
            existingNotifications.collect { list ->
                hasNotifications = list.isNotEmpty()
            }
            
            if (hasNotifications) return
            
            // Busca informações do utilizador
            val user = userRepository.getUserById(userId)
            val username = user?.username ?: "User$userId"
            
            // Cria notificações baseadas no utilizador
            val userNotifications = getUserSpecificNotifications(username)
            
            // Insere notificações na base de dados
            val calendar = Calendar.getInstance()
            
            userNotifications.forEachIndexed { index, (message, type, priority) ->
                // Escalonar datas (mais recentes primeiro)
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
            // Continua sem crashar
        }
    }
    
    /**
     * 📋 Define notificações específicas para cada utilizador
     * Cada utilizador tem um perfil diferente de notificações
     */
    private fun getUserSpecificNotifications(username: String): List<Triple<String, String, String>> {
        return when (username.lowercase()) {
            "osmarg" -> listOf(
                // Notificações para OsmarG - Foco em segurança residencial
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
                // Notificações para DiogoS - Foco em monitorização comercial
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
                // Notificações para Admin - Foco em gestão do sistema
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
                // Notificações genéricas para outros utilizadores
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