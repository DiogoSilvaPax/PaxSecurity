package com.example.projeto.database.dao

import androidx.room.*
import com.example.projeto.database.entities.AuditLog
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_logs ORDER BY action_date DESC")
    fun getAllAuditLogs(): Flow<List<AuditLog>>
    
    @Query("SELECT * FROM audit_logs WHERE user_id = :userId ORDER BY action_date DESC")
    fun getAuditLogsByUserId(userId: Int): Flow<List<AuditLog>>
    
    @Query("SELECT * FROM audit_logs WHERE log_id = :logId")
    suspend fun getAuditLogById(logId: Int): AuditLog?
    
    @Insert
    suspend fun insertAuditLog(auditLog: AuditLog): Long
    
    @Update
    suspend fun updateAuditLog(auditLog: AuditLog)
    
    @Delete
    suspend fun deleteAuditLog(auditLog: AuditLog)
    
    @Query("SELECT * FROM audit_logs WHERE action = :action ORDER BY action_date DESC")
    fun getAuditLogsByAction(action: String): Flow<List<AuditLog>>
    
    @Query("DELETE FROM audit_logs WHERE action_date < :cutoffDate")
    suspend fun deleteOldLogs(cutoffDate: java.util.Date)
}