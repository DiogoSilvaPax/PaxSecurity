package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.House
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
    @Query("SELECT * FROM casas")
    fun getAllHouses(): Flow<List<House>>
    
    @Query("SELECT * FROM casas WHERE house_id = :houseId")
    suspend fun getHouseById(houseId: Int): House?
    
    @Query("SELECT * FROM casas WHERE client_id = :clientId")
    fun getHousesByClientId(clientId: Int): Flow<List<House>>
    
    @Insert
    suspend fun insertHouse(house: House): Long
    
    @Update
    suspend fun updateHouse(house: House)
    
    @Delete
    suspend fun deleteHouse(house: House)
    
    @Query("SELECT * FROM casas WHERE status = :status")
    fun getHousesByStatus(status: String): Flow<List<House>>
}