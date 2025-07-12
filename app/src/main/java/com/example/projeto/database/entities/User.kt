package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * ENTIDADE USER - Representa os utilizadores do sistema
 * 
 * Esta tabela armazena informações dos utilizadores que podem aceder à aplicação.
 * Inclui dados de autenticação, perfil e controlo de acesso.
 */
@Entity(tableName = "contas_utilizador")
data class User(
    // Chave primária - ID único do utilizador
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,
    
    // Nome de utilizador único para login
    @ColumnInfo(name = "username")
    val username: String,
    
    // Password encriptada com SHA-256
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    
    // Email do utilizador
    @ColumnInfo(name = "email")
    val email: String,
    
    // Tipo de utilizador (user, admin, etc.)
    @ColumnInfo(name = "role")
    val role: String = "user",
    
    // Data de criação da conta
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    // Data da última atualização
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    
    // Data do último login (pode ser null)
    @ColumnInfo(name = "last_login")
    val lastLogin: Date? = null,
    
    // Estado da conta (active, inactive, suspended)
    @ColumnInfo(name = "status")
    val status: String = "active",
    
    // URL da foto de perfil (opcional)
    @ColumnInfo(name = "profile_picture")
    val profilePicture: String? = null,
    
    // Ligação ao cliente (se aplicável)
    @ColumnInfo(name = "client_id")
    val clientId: Int? = null
)