package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "contas_utilizador")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,
    
    @ColumnInfo(name = "username")
    val username: String,
    
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    
    @ColumnInfo(name = "email")
    val email: String,
    
    @ColumnInfo(name = "role")
    val role: String = "user",
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    
    @ColumnInfo(name = "last_login")
    val lastLogin: Date? = null,
    
    @ColumnInfo(name = "status")
    val status: String = "active"
)