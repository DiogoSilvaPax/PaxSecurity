package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * ENTIDADE CLIENT - Representa os clientes do sistema de segurança
 * 
 * Esta tabela armazena informações dos clientes que contratam os serviços
 * de segurança. Cada cliente pode ter múltiplas casas e notificações.
 */
@Entity(tableName = "clientes")
data class Client(
    // Chave primária - ID único do cliente
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "client_id")
    val clientId: Int = 0,
    
    // Nome próprio do cliente
    @ColumnInfo(name = "first_name")
    val firstName: String,
    
    // Apelido do cliente
    @ColumnInfo(name = "last_name")
    val lastName: String,
    
    // Email de contacto (único)
    @ColumnInfo(name = "email")
    val email: String,
    
    // Número de telefone
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    
    // Morada completa
    @ColumnInfo(name = "address")
    val address: String,
    
    // Cidade
    @ColumnInfo(name = "city")
    val city: String,
    
    // Distrito/Estado
    @ColumnInfo(name = "state")
    val state: String,
    
    // Código postal
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    
    // Data de registo no sistema
    @ColumnInfo(name = "registration_date")
    val registrationDate: Date = Date()
)