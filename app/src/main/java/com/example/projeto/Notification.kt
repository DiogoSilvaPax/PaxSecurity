package com.example.projeto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto.viewmodel.NotificationViewModel
import com.example.projeto.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * 🔔 TELA DE NOTIFICAÇÕES - Mostra notificações específicas do utilizador
 * 
 * Esta tela:
 * - Carrega notificações do utilizador atual
 * - Mostra loading durante 2 segundos
 * - Exibe notificações personalizadas
 * - Permite marcar como lidas
 */
@Composable
fun NotificationsContent(paddingValues: PaddingValues) {
    // ViewModels
    val notificationViewModel: NotificationViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    
    // Estados observáveis
    val notifications by notificationViewModel.notifications.collectAsState()
    val isLoading by notificationViewModel.isLoading.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()
    
    // Carrega notificações quando a tela abre ou utilizador muda
    LaunchedEffect(currentUser) {
        val userId = currentUser?.userId ?: 1 // Default para OsmarG se não houver utilizador
        notificationViewModel.loadNotificationsForUser(userId)
    }

    // ==================== CABEÇALHO ====================
    
    Column(modifier = Modifier.padding(paddingValues)) {
        // Ícone de notificações
        Icon(
            Icons.Default.Notifications,
            contentDescription = "Notificações",
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth()
                .size(50.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        // Título da tela
        Text(
            text = "Notificações",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        )
        
        // Mostra nome do utilizador atual
        currentUser?.let { user ->
            Text(
                text = "Utilizador: ${user.username}",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }

    // ==================== CONTEÚDO PRINCIPAL ====================
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Espaçamento para o cabeçalho
        Spacer(modifier = Modifier.height(280.dp))

        // Estado de carregamento
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "A carregar notificações...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        } 
        // Lista de notificações
        else if (notifications.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onMarkAsRead = { notificationViewModel.markAsRead(notification.notificationId) }
                    )
                }
            }
        } 
        // Estado vazio
        else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhuma notificação disponível",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 📋 CARD DE NOTIFICAÇÃO - Componente individual para cada notificação
 */
@Composable
fun NotificationCard(
    notification: com.example.projeto.database.entities.NotificationEntity,
    onMarkAsRead: () -> Unit
) {
    // Formatador de data
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                if (!notification.isRead) {
                    onMarkAsRead()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) 
                Color(0xFF1A1A1A) else Color(0xFF2D2D2D)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Data e hora
            Text(
                text = dateFormatter.format(notification.notificationDate),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // Mensagem da notificação
            Text(
                text = notification.message,
                color = if (notification.isRead) 
                    Color.White.copy(alpha = 0.6f) else Color.White,
                fontSize = 14.sp,
                fontWeight = if (notification.isRead) 
                    FontWeight.Normal else FontWeight.Medium
            )
            
            // Indicador de prioridade
            if (notification.priority == "high") {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🔴 ALTA PRIORIDADE",
                    color = Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}