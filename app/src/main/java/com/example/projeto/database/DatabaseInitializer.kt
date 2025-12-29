package com.example.projeto.database

import android.content.Context
import com.example.projeto.database.entities.NotificationEntity
import com.example.projeto.database.entities.User
import com.example.projeto.repository.NotificationRepository
import com.example.projeto.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Calendar
import java.util.Date

class DatabaseInitializer(private val context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val userRepository = UserRepository(database.userDao())
    private val notificationRepository = NotificationRepository(database.notificationDao())

    fun initializeDefaultUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val defaultUsers = listOf(
                    Triple("OsmarG", "osmar123", "osmar@security.com"),
                    Triple("DiogoS", "diogo123", "diogo@security.com"),
                    Triple("admin", "admin123", "admin@security.com"),
                )

                defaultUsers.forEach { (username, password, email) ->
                    val existingUser = userRepository.getUserByUsername(username)

                    if (existingUser == null) {
                        val userId = createUser(username, password, email)
                        createNotificationsForUser(userId.toInt(), username)
                    }
                }

            } catch (e: Exception) {
            }
        }
    }

    private suspend fun createUser(username: String, password: String, email: String): Long {
        val hashedPassword = hashPassword(password)

        val role = when (username) {
            "admin" -> "admin"
            "OsmarG", "DiogoS" -> "manager"
            else -> "user"
        }

        val user = User(
            username = username,
            passwordHash = hashedPassword,
            email = email,
            role = role,
            createdAt = Date(),
            updatedAt = Date(),
            status = "active"
        )

        return userRepository.insertUser(user)
    }

    private suspend fun createNotificationsForUser(userId: Int, username: String) {
        val userNotifications = getUserSpecificNotifications(username)
        val calendar = Calendar.getInstance()

        userNotifications.forEachIndexed { index, (message, type, priority) ->
            calendar.add(Calendar.HOUR, -index)

            val notification = NotificationEntity(
                clientId = userId,
                message = message,
                type = type,
                priority = priority,
                notificationDate = calendar.time,
                createdAt = Date(),
                updatedAt = Date(),
                status = "unread",
                isRead = false
            )

            notificationRepository.insertNotification(notification)
        }
    }

    private fun getUserSpecificNotifications(username: String): List<Triple<String, String, String>> {
        return when (username.lowercase()) {
            "osmarg" -> listOf(
                Triple("Cam 05 - Movimento detectado no quintal", "movement", "high"),
                Triple("Cam 03 - Ligação perdida na sala", "system", "medium"),
                Triple("Cam 01 - Movimento suspeito na entrada", "movement", "high"),
                Triple("Sistema de segurança ativado automaticamente", "system", "normal"),
                Triple("Cam 02 - Bateria baixa no quarto (15%)", "battery", "medium"),
                Triple("Acesso autorizado - Porta principal", "access", "normal"),
                Triple("Manutenção programada para amanhã às 14h", "maintenance", "low"),
                Triple("Todas as câmaras online e funcionais", "system", "normal")
            )

            "diogos" -> listOf(
                Triple("Cam 04 - Movimento no estacionamento", "movement", "high"),
                Triple("Cam 06 - Conexão instável na receção", "system", "medium"),
                Triple("Cam 02 - Múltiplas pessoas detectadas", "movement", "high"),
                Triple("Sistema de alarme ativado - Modo noturno", "system", "normal"),
                Triple("Cam 01 - Bateria crítica (5%) - Substituir", "battery", "high"),
                Triple("Acesso negado - Cartão não reconhecido", "access", "high"),
                Triple("Atualização de firmware disponível", "maintenance", "normal"),
                Triple("Relatório semanal de atividade gerado", "system", "normal")
            )

            "admin" -> listOf(
                Triple("Novo utilizador registado no sistema", "system", "normal"),
                Triple("Relatório de performance - Sistema estável", "system", "low"),
                Triple("Manutenção de servidor agendada", "maintenance", "medium"),
                Triple("Tentativa de login falhada - IP suspeito", "security", "high"),
                Triple("Backup completo do sistema realizado", "system", "normal"),
                Triple("Atualização de segurança instalada", "system", "medium"),
                Triple("Estatísticas mensais disponíveis", "system", "low"),
                Triple("Firewall bloqueou 15 tentativas de acesso", "security", "medium")
            )

            else -> listOf(
                Triple("Bem-vindo ao sistema de segurança", "system", "normal"),
                Triple("Configure as suas preferências", "system", "low")
            )
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}