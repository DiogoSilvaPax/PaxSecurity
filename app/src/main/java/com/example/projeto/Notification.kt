package com.example.projeto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType
)

enum class NotificationType {
    ALERT, WARNING, INFO
}

@Composable
fun NotificationsContent(paddingValues: PaddingValues) {
    var isLoading by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }

    // Simular carregamento e criar notificações
    LaunchedEffect(Unit) {
        delay(2000) // 2 segundos de loading
        
        // Criar notificações simples
        notifications = listOf(
            Notification(
                id = 1,
                title = "Movimento Detectado",
                message = "Cam 05 - Movimento Detectado",
                time = "29/05/2025 18:45",
                type = NotificationType.ALERT
            ),
            Notification(
                id = 2,
                title = "Ligação Perdida",
                message = "Cam 03 - Ligação Perdida",
                time = "25/05/2025 09:32",
                type = NotificationType.WARNING
            ),
            Notification(
                id = 3,
                title = "Movimento Detectado",
                message = "Cam 01 - Movimento Detectado",
                time = "22/03/2025 15:37",
                type = NotificationType.ALERT
            ),
            Notification(
                id = 4,
                title = "Sistema",
                message = "Sistema de segurança ativado",
                time = "20/03/2025 08:15",
                type = NotificationType.INFO
            ),
            Notification(
                id = 5,
                title = "Bateria Baixa",
                message = "Cam 02 - Bateria baixa",
                time = "18/03/2025 14:22",
                type = NotificationType.WARNING
            ),
            Notification(
                id = 6,
                title = "Acesso Autorizado",
                message = "Acesso autorizado na entrada",
                time = "15/03/2025 11:30",
                type = NotificationType.INFO
            ),
            Notification(
                id = 7,
                title = "Manutenção",
                message = "Manutenção programada para amanhã",
                time = "12/03/2025 16:45",
                type = NotificationType.INFO
            ),
            Notification(
                id = 8,
                title = "Conexão Restabelecida",
                message = "Conexão restabelecida com todas as câmaras",
                time = "10/03/2025 09:15",
                type = NotificationType.INFO
            )
        )
        
        isLoading = false
    }

    Column(modifier = Modifier.padding(paddingValues)) {
        Icon(
            Icons.Default.Notifications,
            contentDescription = "notifications",
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth()
                .size(50.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 250.dp)
        ) {
            // Empty row for spacing
        }

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
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "A carregar notificações...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(notification = notification)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Marcar como lida */ },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date and time
            Text(
                text = notification.time,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // Message
            Text(
                text = notification.message,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}