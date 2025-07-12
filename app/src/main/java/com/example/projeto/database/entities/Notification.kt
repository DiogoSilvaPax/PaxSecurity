package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

/**
 * ENTIDADE NOTIFICATION - Representa as notificações do sistema
 * 
 * Armazena todas as notificações enviadas aos clientes sobre eventos
 * de segurança, alertas do sistema, manutenções, etc.
 */
@Entity(
    tableName = "notificacoes",
    foreignKeys = [
        // Relacionamento com Cliente - notificações pertencem a um cliente
        ForeignKey(
            entity = Client::class,
            parentColumns = ["client_id"],
            childColumns = ["client_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NotificationEntity(
    // Chave primária - ID único da notificação
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_id")
    val notificationId: Int = 0,
    
    // Chave estrangeira - ID do cliente que recebe a notificação
    @ColumnInfo(name = "client_id")
    val clientId: Int,
    
    // Conteúdo da mensagem da notificação
    @ColumnInfo(name = "message")
    val message: String,
    
    // Data e hora da notificação
    @ColumnInfo(name = "notification_date")
    val notificationDate: Date = Date(),
    
    // Estado da notificação (read, unread, archived)
    @ColumnInfo(name = "status")
    val status: String = "unread",
    
    // Data de criação do registo
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    // Data da última atualização
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    
    // Tipo de notificação (movement, system, battery, access, maintenance)
    @ColumnInfo(name = "type")
    val type: String,
    
    // Prioridade da notificação (low, normal, medium, high, critical)
    @ColumnInfo(name = "priority")
    val priority: String = "normal",
    
    // Flag booleana para controlo de leitura
    @ColumnInfo(name = "is_read")
    val isRead: Boolean = false
)