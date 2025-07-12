package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

/**
 * ENTIDADE AUDIT_LOG - Registo de auditoria do sistema
 * 
 * Mantém um histórico de todas as ações importantes realizadas no sistema
 * para fins de segurança, debugging e compliance.
 */
@Entity(
    tableName = "audit_logs",
    foreignKeys = [
        // Relacionamento com Utilizador - quem executou a ação
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AuditLog(
    // Chave primária - ID único do log
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    val logId: Int = 0,
    
    // Chave estrangeira - ID do utilizador que executou a ação
    @ColumnInfo(name = "user_id")
    val userId: Int,
    
    // Descrição da ação executada (login, create_client, delete_notification, etc.)
    @ColumnInfo(name = "action")
    val action: String,
    
    // Data e hora da ação
    @ColumnInfo(name = "action_date")
    val actionDate: Date = Date(),
    
    // Endereço IP de onde a ação foi executada
    @ColumnInfo(name = "ip_address")
    val ipAddress: String,
    
    // Estado do resultado da ação (success, failed, error)
    @ColumnInfo(name = "status")
    val status: String,
    
    // Data de criação do registo
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    // Data da última atualização
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    
    // Detalhes adicionais da ação (JSON, texto livre)
    @ColumnInfo(name = "details")
    val details: String? = null,
    
    // Tipo de entidade afetada (User, Client, House, Notification)
    @ColumnInfo(name = "entity_type")
    val entityType: String
)