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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projeto.R

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
    // Sample notifications data
    val notifications = remember {
        listOf(
            Notification(1, "Movimento Detectado", "Câmara 01 - Entrada principal", "10:30", NotificationType.ALERT),
            Notification(2, "Sistema Online", "Todas as câmaras conectadas", "09:15", NotificationType.INFO),
            Notification(3, "Bateria Baixa", "Câmara 03 - Jardim", "08:45", NotificationType.WARNING),
            Notification(4, "Acesso Negado", "Tentativa de login falhada", "08:20", NotificationType.ALERT),
            Notification(5, "Manutenção", "Sistema será atualizado às 02:00", "07:30", NotificationType.INFO),
            Notification(6, "Movimento Detectado", "Câmara 05 - Garagem", "07:15", NotificationType.ALERT)
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
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.security),
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Notificações",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(notification = notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    val backgroundColor = when (notification.type) {
        NotificationType.ALERT -> Color(0xFF8B0000)
        NotificationType.WARNING -> Color(0xFF8B4513)
        NotificationType.INFO -> Color(0xFF2F4F4F)
    }

    val iconColor = when (notification.type) {
        NotificationType.ALERT -> Color(0xFFFF6B6B)
        NotificationType.WARNING -> Color(0xFFFFD93D)
        NotificationType.INFO -> Color(0xFF6BCF7F)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (notification.type) {
                    NotificationType.ALERT -> Icons.Default.Warning
                    NotificationType.WARNING -> Icons.Default.Info
                    NotificationType.INFO -> Icons.Default.CheckCircle
                },
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = notification.message,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Text(
                text = notification.time,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
