package com.example.projeto.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.projeto.database.converters.BigDecimalConverter
import com.example.projeto.database.converters.DateConverter
import com.example.projeto.database.dao.*
import com.example.projeto.database.entities.*

/**
 * BASE DE DADOS PRINCIPAL - Configuração central da base de dados Room
 * 
 * Esta classe define a estrutura completa da base de dados, incluindo:
 * - Todas as entidades (tabelas)
 * - Conversores de tipos
 * - DAOs para acesso aos dados
 * - Configuração singleton
 */
@Database(
    entities = [
        User::class,                // Tabela de utilizadores
        Client::class,              // Tabela de clientes
        House::class,               // Tabela de casas/propriedades
        NotificationEntity::class,  // Tabela de notificações
        AuditLog::class            // Tabela de logs de auditoria
    ],
    version = 1,                   // Versão da base de dados
    exportSchema = false           // Não exportar schema para testes
)
@TypeConverters(DateConverter::class, BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {
    
    // ==================== DAOs ABSTRATOS ====================
    
    /**
     * DAO para operações com utilizadores
     */
    abstract fun userDao(): UserDao
    
    /**
     * DAO para operações com clientes
     */
    abstract fun clientDao(): ClientDao
    
    /**
     * DAO para operações com casas
     */
    abstract fun houseDao(): HouseDao
    
    /**
     * DAO para operações com notificações
     */
    abstract fun notificationDao(): NotificationDao
    
    /**
     * DAO para operações com logs de auditoria
     */
    abstract fun auditLogDao(): AuditLogDao
    
    // ==================== SINGLETON PATTERN ====================
    
    companion object {
        // Instância volátil para thread safety
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Obtém instância única da base de dados
         * Implementa padrão Singleton thread-safe
         * 
         * @param context Contexto da aplicação
         * @return Instância da base de dados
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "security_app_database"  // Nome do ficheiro da BD
                )
                .fallbackToDestructiveMigration()  // Recria BD se houver conflitos de versão
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}