package com.example.projeto

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    ALERT, WARNING, INFO;

    fun getPriority(): String {
        return when (this) {
            ALERT -> "high"
            WARNING -> "medium"
            INFO -> "low"
        }
    }
}

// Cores bem definidas
val RedAlert = Color(0xFFFF1744)
val YellowAlert = Color(0xFFEBCB4A)
val BlueAlert = Color(0xFF0D47A1)

@Composable
fun NotificationsContent(paddingValues: PaddingValues) {
    var isLoading by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }

    LaunchedEffect(Unit) {
        delay(2000)
        notifications = listOf(
            Notification(1, "Movimento Detectado", "Quintal - Movimento Detectado", "29/05/2025 18:45", NotificationType.ALERT),
            Notification(2, "Ligação Perdida", "Quarto - Ligação Perdida", "25/05/2025 09:32", NotificationType.WARNING),
            Notification(3, "Movimento Detectado", "Entrada - Movimento Detectado", "22/03/2025 15:37", NotificationType.ALERT),
            Notification(4, "Sistema", "Sistema de segurança ativado", "20/03/2025 08:15", NotificationType.INFO),
            Notification(5, "Bateria Baixa", "Sala - Bateria baixa", "18/03/2025 14:22", NotificationType.WARNING),
            Notification(6, "Acesso Autorizado", "Acesso autorizado na entrada", "15/03/2025 11:30", NotificationType.INFO),
            Notification(7, "Manutenção", "Manutenção programada para amanhã", "12/03/2025 16:45", NotificationType.INFO),
            Notification(8, "Conexão Restabelecida", "Conexão restabelecida com todas as câmaras", "10/03/2025 09:15", NotificationType.INFO)
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
        Spacer(modifier = Modifier.height(250.dp))

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
    val blinkColor = when (notification.type) {
        NotificationType.ALERT -> RedAlert
        NotificationType.WARNING -> YellowAlert
        NotificationType.INFO -> BlueAlert
    }

    // Define o alpha (animação só para ALERT e WARNING)
    val blinkAlpha: Float = when (notification.type) {
        NotificationType.ALERT -> {
            val infiniteTransition = rememberInfiniteTransition(label = "blink_alert")
            infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha_alert"
            ).value
        }
        NotificationType.WARNING -> {
            val infiniteTransition = rememberInfiniteTransition(label = "blink_warning")
            infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1800, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "alpha_warning"
            ).value
        }
        NotificationType.INFO -> 1f // estático
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.time,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Surface(
                    modifier = Modifier
                        .size(14.dp)
                        .alpha(blinkAlpha),
                    shape = CircleShape,
                    color = blinkColor
                ) {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notification.message,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

