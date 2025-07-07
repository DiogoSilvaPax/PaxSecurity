package com.example.projeto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.database.AppDatabase
import com.example.projeto.database.entities.Client
import com.example.projeto.database.entities.House
import com.example.projeto.database.entities.NotificationEntity
import com.example.projeto.repository.ClientRepository
import com.example.projeto.repository.HouseRepository
import com.example.projeto.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class ClientViewModel(application: Application) : AndroidViewModel(application) {
    
    private val clientRepository: ClientRepository
    private val houseRepository: HouseRepository
    private val notificationRepository: NotificationRepository
    
    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Available camera locations for assignment
    private val availableCameraLocations = listOf(
        "Porta_Entrada", "Sala", "Quartito", "Cozinha", "Quintal", "Estacionamento"
    )
    
    init {
        val database = AppDatabase.getDatabase(application)
        clientRepository = ClientRepository(database.clientDao())
        houseRepository = HouseRepository(database.houseDao())
        notificationRepository = NotificationRepository(database.notificationDao())
        loadClients()
    }
    
    private fun loadClients() {
        viewModelScope.launch {
            clientRepository.getAllClients().collect { clientList ->
                _clients.value = clientList
            }
        }
    }
    
    fun registerClient(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        address: String,
        city: String,
        state: String,
        zipCode: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Check if email already exists
                val existingClient = clientRepository.getClientByEmail(email)
                if (existingClient != null) {
                    onError("Cliente com este email já existe")
                    return@launch
                }
                
                // Create new client
                val client = Client(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phoneNumber = phoneNumber,
                    address = address,
                    city = city,
                    state = state,
                    zipCode = zipCode,
                    registrationDate = Date()
                )
                
                val clientId = clientRepository.insertClient(client).toInt()
                
                // Create a house for the client
                createHouseForClient(clientId, address, city, state, zipCode)
                
                // Create dummy notifications for the client
                createDummyNotifications(clientId)
                
                onSuccess()
            } catch (e: Exception) {
                onError("Erro ao registar cliente: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun createHouseForClient(
        clientId: Int,
        address: String,
        city: String,
        state: String,
        zipCode: String
    ) {
        val house = House(
            clientId = clientId,
            houseType = "Residencial",
            address = address,
            city = city,
            state = state,
            zipCode = zipCode,
            value = BigDecimal("250000.00"), // Default value
            status = "active"
        )
        houseRepository.insertHouse(house)
    }
    
    private suspend fun createDummyNotifications(clientId: Int) {
        // Get half of the available camera locations for this client
        val assignedCameras = availableCameraLocations.shuffled().take(3)
        
        val dummyNotifications = listOf(
            Triple("Movimento detectado na ${assignedCameras[0]}", "movement", "high"),
            Triple("Sistema de segurança ativado", "system", "normal"),
            Triple("Bateria baixa na câmara ${assignedCameras[1]}", "battery", "medium"),
            Triple("Acesso autorizado na ${assignedCameras[2]}", "access", "normal"),
            Triple("Manutenção programada para amanhã", "maintenance", "low"),
            Triple("Conexão restabelecida com todas as câmaras", "system", "normal"),
            Triple("Movimento detectado na ${assignedCameras[0]} às 14:30", "movement", "high"),
            Triple("Backup de dados concluído", "system", "low"),
            Triple("Tentativa de acesso não autorizado", "access", "high"),
            Triple("Atualização de firmware disponível", "system", "low")
        )
        
        dummyNotifications.forEach { (message, type, priority) ->
            val notification = NotificationEntity(
                clientId = clientId,
                message = message,
                type = type,
                priority = priority,
                notificationDate = Date(),
                createdAt = Date(),
                updatedAt = Date(),
                status = "unread",
                isRead = false
            )
            notificationRepository.insertNotification(notification)
        }
    }
    
    fun deleteClient(client: Client) {
        viewModelScope.launch {
            clientRepository.deleteClient(client)
        }
    }
    
    fun searchClients(query: String) {
        viewModelScope.launch {
            clientRepository.searchClients("%$query%").collect { searchResults ->
                _clients.value = searchResults
            }
        }
    }
}