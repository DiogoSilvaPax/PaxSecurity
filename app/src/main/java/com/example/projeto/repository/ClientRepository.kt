package com.example.projeto.repository

import com.example.projeto.database.dao.ClientDao
import com.example.projeto.database.entities.Client
import kotlinx.coroutines.flow.Flow

class ClientRepository(private val clientDao: ClientDao) {
    
    fun getAllClients(): Flow<List<Client>> = clientDao.getAllClients()
    
    suspend fun getClientById(clientId: Int): Client? = clientDao.getClientById(clientId)
    
    suspend fun getClientByEmail(email: String): Client? = clientDao.getClientByEmail(email)
    
    suspend fun insertClient(client: Client): Long = clientDao.insertClient(client)
    
    suspend fun updateClient(client: Client) = clientDao.updateClient(client)
    
    suspend fun deleteClient(client: Client) = clientDao.deleteClient(client)
    
    fun searchClients(searchQuery: String): Flow<List<Client>> = clientDao.searchClients(searchQuery)
}