package com.example.projeto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projeto.viewmodel.UserViewModel
import android.app.Activity

@Composable
fun Logout(navController: NavController, onLogout: () -> Unit = {}) {
    val userViewModel: UserViewModel = viewModel()
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Logout") },
            text = { Text("Tem certeza que deseja terminar a sessão?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Terminar Sessão",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { 
                showConfirmDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Logout")
        }
    }
}
