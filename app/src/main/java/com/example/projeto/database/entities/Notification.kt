package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "notificacoes",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["client_id"],
            childColumns = ["client_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_id")
    val notificationId: Int = 0,
    
    @ColumnInfo(name = "client_id")
    val clientId: Int,
    
    @ColumnInfo(name = "message")
    val message: String,
    
    @ColumnInfo(name = "notification_date")
    val notificationDate: Date = Date(),
    
    @ColumnInfo(name = "status")
    val status: String = "unread",
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    
    @ColumnInfo(name = "type")
    val type: String,
    
    @ColumnInfo(name = "priority")
    val priority: String = "normal",
    
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false
)