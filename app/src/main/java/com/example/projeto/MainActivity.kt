package com.example.projeto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoggedIn by remember { mutableStateOf(false) }

            if (isLoggedIn) {
                MainScreen()
            } else {
                LoginScreen { isLoggedIn = true }
            }
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.security),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )


        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogin,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6482F))
        ) {
            Text("Login", color = Color.White)
        }
    }
}



@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { 
                        Icon(
                            Icons.Default.Notifications, 
                            contentDescription = "Alertas",
                            tint = if (selectedTab == 0) Color.Black else Color.White
                        ) 
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.videocam),
                            contentDescription = "Câmaras",
                            tint = if (selectedTab == 1) Color.Black else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { 
                        Icon(
                            Icons.Default.Settings, 
                            contentDescription = "Definições",
                            tint = if (selectedTab == 2) Color.Black else Color.White
                        ) 
                    }
                )
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        when (selectedTab) {
            0 -> NotificationsContent(paddingValues)
            1 -> CameraContent(paddingValues)
            2 -> SettingsContent(paddingValues)
        }
    }
}

// Data class for notifications
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

@Composable
fun CameraContent(paddingValues: PaddingValues) {
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
                text = "Câmaras",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(6) { index ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cam 0${index + 1}", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SettingsContent(paddingValues: PaddingValues) {
    var notificationsEnabled by remember { mutableStateOf(false) }
    var darkThemeEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Header
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
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Definições",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Secção: Conta
        SectionTitle("Conta")
        SettingsItem("Alterar palavra-passe", icon = painterResource(R.drawable.security)) //Icons.Default.Lock)
        SettingsItem("Alterar email", icon = painterResource(R.drawable.security))
        SettingsItem("Terminar Sessão", icon = painterResource(R.drawable.security))

        // Secção: Notificações
        SectionTitle("Notificações")
        ToggleItem("Ativar notificações", Icons.Default.Notifications, notificationsEnabled) {
            notificationsEnabled = it
        }

        // Secção: Visualização
//        SectionTitle("Visualização")
//        ToggleItem("Tema Escuro", Icons.Default.DarkMode, darkThemeEnabled) {
//            darkThemeEnabled = it
//        }

        // Secção: Sobre
        SectionTitle("Sobre")
        StaticItem("Versão 1.0.0", Icons.Default.Info)
        SettingsItem("Contactar Suporte", painterResource(R.drawable.security))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(title: String, icon: Painter) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ToggleItem(title: String, icon: ImageVector, state: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = state,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFE6482F),
                    uncheckedThumbColor = Color.Gray
                )
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun StaticItem(title: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
