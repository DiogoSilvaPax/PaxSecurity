package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "casas",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["client_id"],
            childColumns = ["client_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class House(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "house_id")
    val houseId: Int = 0,
    
    @ColumnInfo(name = "client_id")
    val clientId: Int,
    
    @ColumnInfo(name = "house_type")
    val houseType: String,
    
    @ColumnInfo(name = "address")
    val address: String,
    
    @ColumnInfo(name = "city")
    val city: String,
    
    @ColumnInfo(name = "state")
    val state: String,
    
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    
    @ColumnInfo(name = "value")
    val value: BigDecimal,
    
    @ColumnInfo(name = "status")
    val status: String = "active"
)