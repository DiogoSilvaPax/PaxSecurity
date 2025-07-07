package com.example.projeto.repository

import com.example.projeto.database.dao.NotificationDao
import com.example.projeto.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

class NotificationRepository(private val notificationDao: NotificationDao) {
    
    fun getAllNotifications(): Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    
    fun getNotificationsByClientId(clientId: Int): Flow<List<NotificationEntity>> = 
        notificationDao.getNotificationsByClientId(clientId)
    
    suspend fun getNotificationById(notificationId: Int): NotificationEntity? = 
        notificationDao.getNotificationById(notificationId)
    
    fun getUnreadNotifications(): Flow<List<NotificationEntity>> = 
        notificationDao.getUnreadNotifications()
    
    suspend fun insertNotification(notification: NotificationEntity): Long = 
        notificationDao.insertNotification(notification)
    
    suspend fun updateNotification(notification: NotificationEntity) = 
        notificationDao.updateNotification(notification)
    
    suspend fun deleteNotification(notification: NotificationEntity) = 
        notificationDao.deleteNotification(notification)
    
    suspend fun markAsRead(notificationId: Int) = 
        notificationDao.markAsRead(notificationId)
    
    suspend fun markAllAsReadForClient(clientId: Int) = 
        notificationDao.markAllAsReadForClient(clientId)
    
    fun getUnreadCount(clientId: Int): Flow<Int> = 
        notificationDao.getUnreadCount(clientId)
    
    suspend fun createNotification(
        clientId: Int,
        message: String,
        type: String,
        priority: String = "normal"
    ): Long {
        val notification = NotificationEntity(
            clientId = clientId,
            message = message,
            type = type,
            priority = priority,
            notificationDate = Date(),
            createdAt = Date(),
            updatedAt = Date()
        )
        return insertNotification(notification)
    }
}