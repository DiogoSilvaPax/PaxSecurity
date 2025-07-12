package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.Client
import kotlinx.coroutines.flow.Flow

/**
 * DAO CLIENT - Interface de acesso aos dados dos clientes
 * 
 * Define todas as operações de base de dados relacionadas com clientes.
 * Inclui operações CRUD e queries de pesquisa.
 */
@Dao
interface ClientDao {
    
    // ==================== CONSULTAS (READ) ====================
    
    /**
     * Obtém todos os clientes do sistema
     * @return Flow com lista de clientes (atualiza automaticamente)
     */
    @Query("SELECT * FROM clientes")
    fun getAllClients(): Flow<List<Client>>
    
    /**
     * Busca cliente por ID
     * @param clientId ID do cliente
     * @return Cliente encontrado ou null
     */
    @Query("SELECT * FROM clientes WHERE client_id = :clientId")
    suspend fun getClientById(clientId: Int): Client?
    
    /**
     * Busca cliente por email (único)
     * @param email Email do cliente
     * @return Cliente encontrado ou null
     */
    @Query("SELECT * FROM clientes WHERE email = :email")
    suspend fun getClientByEmail(email: String): Client?
    
    /**
     * Pesquisa clientes por nome, apelido ou email
     * @param searchQuery Termo de pesquisa (com wildcards %)
     * @return Flow com resultados da pesquisa
     */
    @Query("SELECT * FROM clientes WHERE first_name LIKE :searchQuery OR last_name LIKE :searchQuery OR email LIKE :searchQuery")
    fun searchClients(searchQuery: String): Flow<List<Client>>
    
    // ==================== INSERÇÃO (CREATE) ====================
    
    /**
     * Insere novo cliente na base de dados
     * @param client Dados do cliente
     * @return ID do cliente criado
     */
    @Insert
    suspend fun insertClient(client: Client): Long
    
    // ==================== ATUALIZAÇÃO (UPDATE) ====================
    
    /**
     * Atualiza dados completos do cliente
     * @param client Cliente com dados atualizados
     */
    @Update
    suspend fun updateClient(client: Client)
    
    // ==================== ELIMINAÇÃO (DELETE) ====================
    
    /**
     * Elimina cliente da base de dados
     * NOTA: Também elimina casas e notificações relacionadas (CASCADE)
     * @param client Cliente a eliminar
     */
    @Delete
    suspend fun deleteClient(client: Client)
}