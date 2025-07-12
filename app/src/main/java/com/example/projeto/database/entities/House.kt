package com.example.projeto.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

/**
 * ENTIDADE HOUSE - Representa as casas/propriedades dos clientes
 * 
 * Cada cliente pode ter múltiplas propriedades onde são instalados
 * sistemas de segurança. Esta tabela armazena detalhes de cada propriedade.
 */
@Entity(
    tableName = "casas",
    foreignKeys = [
        // Relacionamento com Cliente - se cliente for eliminado, casa também é
        ForeignKey(
            entity = Client::class,
            parentColumns = ["client_id"],
            childColumns = ["client_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class House(
    // Chave primária - ID único da casa
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "house_id")
    val houseId: Int = 0,
    
    // Chave estrangeira - ID do cliente proprietário
    @ColumnInfo(name = "client_id")
    val clientId: Int,
    
    // Tipo de propriedade (Residencial, Comercial, etc.)
    @ColumnInfo(name = "house_type")
    val houseType: String,
    
    // Morada da propriedade
    @ColumnInfo(name = "address")
    val address: String,
    
    // Cidade da propriedade
    @ColumnInfo(name = "city")
    val city: String,
    
    // Distrito/Estado da propriedade
    @ColumnInfo(name = "state")
    val state: String,
    
    // Código postal da propriedade
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    
    // Valor estimado da propriedade
    @ColumnInfo(name = "value")
    val value: BigDecimal,
    
    // Estado da propriedade (active, inactive, maintenance)
    @ColumnInfo(name = "status")
    val status: String = "active"
)