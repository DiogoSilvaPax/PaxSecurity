package com.example.projeto.database

import android.content.Context
import com.example.projeto.database.entities.User
import com.example.projeto.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Date

/**
 * 🚀 INICIALIZADOR DA BASE DE DADOS
 * 
 * Esta classe é responsável por:
 * - Criar utilizadores padrão no sistema
 * - Configurar dados iniciais
 * - Garantir que a aplicação tem dados para funcionar
 */
class DatabaseInitializer(private val context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val userRepository = UserRepository(database.userDao())
    
    /**
     * 👥 Inicializa utilizadores padrão do sistema
     * Cria diferentes tipos de utilizadores para demonstração
     */
    fun initializeDefaultUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Lista de utilizadores padrão para criar
                val defaultUsers = listOf(
                    Triple("OsmarG", "osmar123", "osmar@security.com"),
                    Triple("DiogoS", "diogo123", "diogo@security.com"),
                    Triple("admin", "admin123", "admin@security.com"),
                    Triple("cliente1", "cliente123", "cliente1@email.com"),
                    Triple("cliente2", "cliente456", "cliente2@email.com")
                )
                
                // 🔍 DEBUG: Vamos verificar se os utilizadores são criados
                println("🔧 [DEBUG] Iniciando criação de utilizadores...")
                
                // Cria cada utilizador se não existir
                defaultUsers.forEach { (username, password, email) ->
                    val existingUser = userRepository.getUserByUsername(username)
                    
                    if (existingUser == null) {
                        val userId = createUser(username, password, email)
                        println("✅ [DEBUG] Utilizador criado: $username (ID: $userId)")
                        
                        // 🔍 Verificar se a password foi encriptada corretamente
                        val hashedPassword = hashPassword(password)
                        println("🔐 [DEBUG] Password original: $password")
                        println("🔐 [DEBUG] Password encriptada: $hashedPassword")
                        
                    } else {
                        println("ℹ️ [DEBUG] Utilizador já existe: $username (ID: ${existingUser.userId})")
                        
                        // 🔍 Verificar password do utilizador existente
                        println("🔐 [DEBUG] Password na BD: ${existingUser.passwordHash}")
                        println("🔐 [DEBUG] Password esperada: ${hashPassword(password)}")
                        println("🔐 [DEBUG] Passwords coincidem: ${existingUser.passwordHash == hashPassword(password)}")
                    }
                }
                
                // 🧪 Teste específico para admin
                println("\n🧪 [TESTE ADMIN] Verificando credenciais do admin...")
                val adminUser = userRepository.getUserByUsername("admin")
                if (adminUser != null) {
                    val testPassword = "admin123"
                    val hashedTestPassword = hashPassword(testPassword)
                    val authResult = userRepository.authenticateUser("admin", testPassword)
                    
                    println("👤 [ADMIN] Utilizador encontrado: ${adminUser.username}")
                    println("🔐 [ADMIN] Password na BD: ${adminUser.passwordHash}")
                    println("🔐 [ADMIN] Password teste: $hashedTestPassword")
                    println("✅ [ADMIN] Autenticação: ${if (authResult != null) "SUCESSO" else "FALHOU"}")
                } else {
                    println("❌ [ADMIN] Utilizador admin não encontrado!")
                }
                
            } catch (e: Exception) {
                println("❌ [ERROR] Erro ao inicializar utilizadores: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 👤 Cria um novo utilizador na base de dados
     */
    private suspend fun createUser(username: String, password: String, email: String): Long {
        val hashedPassword = hashPassword(password)
        
        // Define role baseado no username
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
    
    /**
     * 🔐 Encripta password usando SHA-256
     * IMPORTANTE: Deve ser igual ao método no UserRepository
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}