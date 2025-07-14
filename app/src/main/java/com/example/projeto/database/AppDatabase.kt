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

@Database(
    entities = [
        User::class,
        Client::class,
        House::class,
        NotificationEntity::class,
        AuditLog::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class, BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun clientDao(): ClientDao
    abstract fun houseDao(): HouseDao
    abstract fun notificationDao(): NotificationDao
    abstract fun auditLogDao(): AuditLogDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "security_app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}