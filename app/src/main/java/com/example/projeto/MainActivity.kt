package com.example.projeto

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projeto.ui.theme.ThemeManager


class MainActivity : ComponentActivity() {
    private val themeManager = ThemeManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(themeManager = themeManager)
            }
        }
    }
}


@Composable
fun MainScreen(themeManager: ThemeManager, onLogout: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Alertas",
                            tint = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
                            tint = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
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
                            tint = if (selectedTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when (selectedTab) {
            0 -> CameraContent(paddingValues = paddingValues)
            1 -> NotificationsContent(paddingValues = paddingValues)
            2 -> SettingsPage(themeManager = themeManager, onLogout = onLogout)
        }
    }
}




// Data class for notifications



