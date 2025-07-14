package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    
    @Query("SELECT * FROM clientes")
    fun getAllClients(): Flow<List<Client>>
    
    @Query("SELECT * FROM clientes WHERE client_id = :clientId")
    suspend fun getClientById(clientId: Int): Client?
    
    @Query("SELECT * FROM clientes WHERE email = :email")
    suspend fun getClientByEmail(email: String): Client?
    
    @Query("SELECT * FROM clientes WHERE first_name LIKE :searchQuery OR last_name LIKE :searchQuery OR email LIKE :searchQuery")
    fun searchClients(searchQuery: String): Flow<List<Client>>
    
    @Insert
    suspend fun insertClient(client: Client): Long
    
    @Update
    suspend fun updateClient(client: Client)
    
    @Delete
    suspend fun deleteClient(client: Client)
}