package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "audit_logs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AuditLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    val logId: Int = 0,
    
    @ColumnInfo(name = "user_id")
    val userId: Int,
    
    @ColumnInfo(name = "action")
    val action: String,
    
    @ColumnInfo(name = "action_date")
    val actionDate: Date = Date(),
    
    @ColumnInfo(name = "ip_address")
    val ipAddress: String,
    
    @ColumnInfo(name = "status")
    val status: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    
    @ColumnInfo(name = "entity_type")
    val entityType: String
)