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
import com.example.projeto.viewmodel.UserViewModel

@Composable
fun ChangeEmail(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()
    var email by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    
    LaunchedEffect(currentUser) {
        currentUser?.let {
            email = it.email
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Alterar Email",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Novo email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { 
                userViewModel.updateEmail(email)
                showSuccess = true
            }, 
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
        
        if (showSuccess) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email atualizado com sucesso!", color = Color.Green)
        }
    }
}
