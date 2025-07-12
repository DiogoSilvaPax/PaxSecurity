package com.example.projeto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projeto.viewmodel.ClientViewModel

/**
 * 📋 REGISTO DE CLIENTES - Formulário para novos clientes
 * 
 * Esta tela permite registar novos clientes no sistema de segurança.
 * Quando um cliente é registado, o sistema automaticamente:
 * 
 * 1. 👤 Cria perfil do cliente na base de dados
 * 2. 🏠 Associa uma casa/propriedade ao cliente  
 * 3. 📹 Atribui câmaras específicas baseadas na localização
 * 4. 🔔 Gera notificações personalizadas
 * 5. 📊 Cria logs de auditoria
 * 
 * É usado por administradores para expandir a base de clientes.
 */

@Composable
fun ClientRegistration(navController: NavController) {
    // ==================== VIEWMODEL E ESTADOS ====================
    
    val clientViewModel: ClientViewModel = viewModel()
    
    // Estados dos campos do formulário
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    
    // Estados de controlo
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // ==================== INTERFACE DO UTILIZADOR ====================
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ==================== CABEÇALHO ====================
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Registo de Cliente",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==================== CAMPOS DO FORMULÁRIO ====================
        
        // 👤 Nome próprio
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 👤 Apelido
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apelido") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📧 Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📞 Telefone
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Telefone") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🏠 Morada
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Morada") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🏙️ Cidade
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🗺️ Distrito
        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text("Distrito") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📮 Código Postal
        OutlinedTextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text("Código Postal") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ==================== BOTÃO DE REGISTO ====================
        
        Button(
            onClick = {
                // 🔍 Validação dos campos obrigatórios
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
                    isLoading = true
                    
                    // 📝 Chama o ViewModel para registar o cliente
                    clientViewModel.registerClient(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        phoneNumber = phoneNumber,
                        address = address,
                        city = city,
                        state = state,
                        zipCode = zipCode,
                        onSuccess = {
                            // ✅ Sucesso - Limpa formulário e mostra mensagem
                            isLoading = false
                            showSuccess = true
                            errorMessage = ""
                            
                            // Limpa todos os campos
                            firstName = ""
                            lastName = ""
                            email = ""
                            phoneNumber = ""
                            address = ""
                            city = ""
                            state = ""
                            zipCode = ""
                        },
                        onError = { error ->
                            // ❌ Erro - Mostra mensagem de erro
                            isLoading = false
                            errorMessage = error
                            showSuccess = false
                        }
                    )
                } else {
                    // ⚠️ Campos obrigatórios em falta
                    errorMessage = "Por favor, preencha os campos obrigatórios (Nome, Apelido, Email)"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                // 🔄 Indicador de carregamento
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text("Registar Cliente")
            }
        }

        // ==================== MENSAGENS DE FEEDBACK ====================
        
        // ✅ Mensagem de sucesso
        if (showSuccess) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Cliente registado com sucesso! Notificações e câmaras foram automaticamente associadas.",
                color = Color.Green
            )
        }

        // ❌ Mensagem de erro
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(errorMessage, color = Color.Red)
        }
    }
}

/**
 * 📋 RESUMO DO QUE ACONTECE QUANDO REGISTAS UM CLIENTE:
 * 
 * 1. 👤 CLIENTE: Cria perfil na tabela 'clientes'
 * 2. 🏠 CASA: Cria casa associada na tabela 'casas'  
 * 3. 📹 CÂMARAS: Atribui câmaras baseadas na localização
 * 4. 🔔 NOTIFICAÇÕES: Gera notificações personalizadas
 * 5. 📊 LOGS: Cria registos de auditoria
 * 
 * É uma operação completa que prepara tudo para o novo cliente!
 */