package com.example.projeto.repository

import com.example.projeto.database.dao.HouseDao
import com.example.projeto.database.entities.House
import kotlinx.coroutines.flow.Flow

class HouseRepository(private val houseDao: HouseDao) {
    
    fun getAllHouses(): Flow<List<House>> = houseDao.getAllHouses()
    
    suspend fun getHouseById(houseId: Int): House? = houseDao.getHouseById(houseId)
    
    fun getHousesByClientId(clientId: Int): Flow<List<House>> = houseDao.getHousesByClientId(clientId)
    
    suspend fun insertHouse(house: House): Long = houseDao.insertHouse(house)
    
    suspend fun updateHouse(house: House) = houseDao.updateHouse(house)
    
    suspend fun deleteHouse(house: House) = houseDao.deleteHouse(house)
    
    fun getHousesByStatus(status: String): Flow<List<House>> = houseDao.getHousesByStatus(status)
}