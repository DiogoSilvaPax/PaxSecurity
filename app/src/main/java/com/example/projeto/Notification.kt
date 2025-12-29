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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto.database.entities.NotificationEntity
import com.example.projeto.viewmodel.NotificationViewModel
import com.example.projeto.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale

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

val RedAlert = Color(0xFFFF1744)
val YellowAlert = Color(0xFFEBCB4A)
val BlueAlert = Color(0xFF0D47A1)

@Composable
fun NotificationsContent(paddingValues: PaddingValues, userViewModel: UserViewModel) {
    val notificationViewModel: NotificationViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()
    val notificationsFromDb by notificationViewModel.notifications.collectAsState()
    val isLoading by notificationViewModel.isLoading.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            notificationViewModel.loadNotificationsForUser(user.userId)
        }
    }

    val notifications = remember(notificationsFromDb) {
        notificationsFromDb.map { it.toNotification() }
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
fun NotificationCard(notification: Notification, onMarkAsRead: () -> Unit = {}) {
    val blinkColor = when (notification.type) {
        NotificationType.ALERT -> RedAlert
        NotificationType.WARNING -> YellowAlert
        NotificationType.INFO -> BlueAlert
    }

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
        NotificationType.INFO -> 1f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMarkAsRead() },
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

fun NotificationEntity.toNotification(): Notification {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedTime = dateFormat.format(notificationDate)

    val notifType = when (priority) {
        "high" -> NotificationType.ALERT
        "medium" -> NotificationType.WARNING
        else -> NotificationType.INFO
    }

    return Notification(
        id = notificationId,
        title = type,
        message = message,
        time = formattedTime,
        type = notifType
    )
}

