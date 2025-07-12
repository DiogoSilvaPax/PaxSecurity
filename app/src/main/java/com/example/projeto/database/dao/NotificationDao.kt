package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO NOTIFICATION - Interface de acesso aos dados das notificações
 * 
 * Define todas as operações de base de dados relacionadas com notificações.
 * Inclui operações CRUD, marcação de leitura e contagens.
 */
@Dao
interface NotificationDao {
    
    // ==================== CONSULTAS (READ) ====================
    
    /**
     * Obtém todas as notificações ordenadas por data (mais recentes primeiro)
     * @return Flow com lista de notificações
     */
    @Query("SELECT * FROM notificacoes ORDER BY notification_date DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    /**
     * Obtém notificações de um cliente específico
     * @param clientId ID do cliente
     * @return Flow com notificações do cliente
     */
    @Query("SELECT * FROM notificacoes WHERE client_id = :clientId ORDER BY notification_date DESC")
    fun getNotificationsByClientId(clientId: Int): Flow<List<NotificationEntity>>
    
    /**
     * Busca notificação por ID
     * @param notificationId ID da notificação
     * @return Notificação encontrada ou null
     */
    @Query("SELECT * FROM notificacoes WHERE notification_id = :notificationId")
    suspend fun getNotificationById(notificationId: Int): NotificationEntity?
    
    /**
     * Obtém apenas notificações não lidas
     * @return Flow com notificações não lidas
     */
    @Query("SELECT * FROM notificacoes WHERE is_read = 0 ORDER BY notification_date DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    
    /**
     * Conta notificações não lidas de um cliente
     * @param clientId ID do cliente
     * @return Flow com número de notificações não lidas
     */
    @Query("SELECT COUNT(*) FROM notificacoes WHERE is_read = 0 AND client_id = :clientId")
    fun getUnreadCount(clientId: Int): Flow<Int>
    
    // ==================== INSERÇÃO (CREATE) ====================
    
    /**
     * Insere nova notificação na base de dados
     * @param notification Dados da notificação
     * @return ID da notificação criada
     */
    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long
    
    // ==================== ATUALIZAÇÃO (UPDATE) ====================
    
    /**
     * Atualiza dados completos da notificação
     * @param notification Notificação com dados atualizados
     */
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    /**
     * Marca uma notificação específica como lida
     * @param notificationId ID da notificação
     */
    @Query("UPDATE notificacoes SET is_read = 1, status = 'read' WHERE notification_id = :notificationId")
    suspend fun markAsRead(notificationId: Int)
    
    /**
     * Marca todas as notificações de um cliente como lidas
     * @param clientId ID do cliente
     */
    @Query("UPDATE notificacoes SET is_read = 1, status = 'read' WHERE client_id = :clientId")
    suspend fun markAllAsReadForClient(clientId: Int)
    
    // ==================== ELIMINAÇÃO (DELETE) ====================
    
    /**
     * Elimina notificação da base de dados
     * @param notification Notificação a eliminar
     */
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
}