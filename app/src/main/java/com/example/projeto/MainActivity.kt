package com.example.projeto

import NotificationsContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
        setContent {
            MaterialTheme {
                Surface(color = Color.DarkGray) {
                    CameraContent() // ← aqui está o nome correto
                }
            }
        }
    }
}

@Composable
fun MainScreen(onLogout: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black
            ) {
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Alertas",
                            tint = if (selectedTab == 1) Color.Black else Color.White
                        )
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.videocam),
                            contentDescription = "Câmaras",
                            tint = if (selectedTab == 0) Color.Black else Color.White,
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
            0 -> CameraContent(paddingValues = paddingValues)
            1 -> NotificationsContent(paddingValues = paddingValues)
            2 -> SettingsPage(onLogout = onLogout)
        }
    }
}




// Data class for notifications



