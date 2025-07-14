package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "clientes")
data class Client(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "client_id")
    val clientId: Int = 0,
    
    @ColumnInfo(name = "first_name")
    val firstName: String,
    
    @ColumnInfo(name = "last_name")
    val lastName: String,
    
    @ColumnInfo(name = "email")
    val email: String,
    
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    
    @ColumnInfo(name = "address")
    val address: String,
    
    @ColumnInfo(name = "city")
    val city: String,
    
    @ColumnInfo(name = "state")
    val state: String,
    
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    
    @ColumnInfo(name = "registration_date")
    val registrationDate: Date = Date()
)