package com.example.projeto.utils

import com.example.projeto.Notification
import com.example.projeto.NotificationType
import com.example.projeto.database.entities.NotificationEntity
import java.text.SimpleDateFormat
import java.util.Locale

object NotificationMapper {
    
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    fun fromEntity(entity: NotificationEntity): Notification {
        return Notification(
            id = entity.notificationId,
            title = getNotificationTitle(entity.type),
            message = entity.message,
            time = timeFormat.format(entity.notificationDate),
            type = mapNotificationType(entity.type)
        )
    }
    
    fun toEntity(notification: Notification, clientId: Int): NotificationEntity {
        return NotificationEntity(
            notificationId = notification.id,
            clientId = clientId,
            message = notification.message,
            type = notification.type.name.lowercase(),
            priority = when (notification.type) {
                NotificationType.ALERT -> "high"
                NotificationType.WARNING -> "medium"
                NotificationType.INFO -> "low"
            }
        )
    }
    
    private fun getNotificationTitle(type: String): String {
        return when (type.lowercase()) {
            "movement" -> "Movimento Detectado"
            "system" -> "Sistema"
            "battery" -> "Bateria"
            "access" -> "Acesso"
            "maintenance" -> "Manutenção"
            else -> "Notificação"
        }
    }
    
    private fun mapNotificationType(type: String): NotificationType {
        return when (type.lowercase()) {
            "movement", "access" -> NotificationType.ALERT
            "battery", "maintenance" -> NotificationType.WARNING
            "system" -> NotificationType.INFO
            else -> NotificationType.INFO
        }
    }
}