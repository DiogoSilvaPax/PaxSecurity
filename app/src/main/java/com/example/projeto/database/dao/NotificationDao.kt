package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notificacoes ORDER BY notification_date DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notificacoes WHERE client_id = :clientId ORDER BY notification_date DESC")
    fun getNotificationsByClientId(clientId: Int): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notificacoes WHERE notification_id = :notificationId")
    suspend fun getNotificationById(notificationId: Int): NotificationEntity?
    
    @Query("SELECT * FROM notificacoes WHERE is_read = 0 ORDER BY notification_date DESC")
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    
    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("UPDATE notificacoes SET is_read = 1, status = 'read' WHERE notification_id = :notificationId")
    suspend fun markAsRead(notificationId: Int)
    
    @Query("UPDATE notificacoes SET is_read = 1, status = 'read' WHERE client_id = :clientId")
    suspend fun markAllAsReadForClient(clientId: Int)
    
    @Query("SELECT COUNT(*) FROM notificacoes WHERE is_read = 0 AND client_id = :clientId")
    fun getUnreadCount(clientId: Int): Flow<Int>
}