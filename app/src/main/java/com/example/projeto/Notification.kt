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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SimpleNotification(
    val id: Int,
    val message: String,
    val date: String,
    val time: String
)

@Composable
fun NotificationsContent(paddingValues: PaddingValues) {
    // Sample notifications - static for now to avoid crashes
    val notifications = remember {
        listOf(
            SimpleNotification(1, "Cam 05 - Movimento Detectado", "29/05/2025", "18:45"),
            SimpleNotification(2, "Cam 03 - Ligação Perdida", "25/05/2025", "09:32"),
            SimpleNotification(3, "Cam 01 - Movimento Detectado", "22/03/2025", "15:37"),
            SimpleNotification(4, "Sistema de segurança ativado", "20/03/2025", "08:15"),
            SimpleNotification(5, "Cam 02 - Bateria baixa", "18/03/2025", "14:22")
        )
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { notification ->
                SimpleNotificationCard(notification = notification)
            }
        }
    }
}

@Composable
fun SimpleNotificationCard(notification: SimpleNotification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
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
                text = "${notification.date} ${notification.time}",
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