package com.example.projeto

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projeto.ui.theme.ThemeManager

// --- NAVIGATION ---
@Composable
fun SettingsPage(themeManager: ThemeManager, onLogout: () -> Unit = {}) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "settings") {
        composable("settings") { SettingsContent(navController, themeManager, onLogout) }
        composable("change_password") { ChangePassword(navController) }
        composable("change_email") { ChangeEmail(navController) }
        composable("logout") { Logout(navController, onLogout) }
        composable("support") { Support(navController) }
        composable("client_registration") { ClientRegistration(navController) }
    }
}

// --- SETTINGS SCREEN ---
@Composable
fun SettingsContent(navController: NavController, themeManager: ThemeManager, onLogout: () -> Unit = {}) {
    var notificationsEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "definições",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 75.dp)
                .size(50.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Definições",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp)
        )

        SettingsSection(title = "Conta") {
            SettingsItem("Alterar palavra-passe", Icons.Default.Lock) {
                navController.navigate("change_password")
            }
            SettingsItem("Alterar email", Icons.Default.Email) {
                navController.navigate("change_email")
            }
            SettingsItem("Terminar Sessão", Icons.Default.Logout) {
                navController.navigate("logout")
            }
        }

        SettingsSection(title = "Notificações") {
            SwitchItem(
                title = "Ativar notificações",
                icon = Icons.Default.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }

        SettingsSection(title = "Visualização") {
            SwitchItem(
                title = "Tema Escuro",
                icon = Icons.Default.Visibility,
                checked = themeManager.isDarkTheme,
                onCheckedChange = { themeManager.toggleTheme() }
            )
        }

        SettingsSection(title = "Clientes") {
            SettingsItem("Registar Cliente", Icons.Default.PersonAdd) {
                navController.navigate("client_registration")
            }
        }

        SettingsSection(title = "Sobre") {
            SettingsItem("Versão 1.0.0", Icons.Default.Settings, enabled = false)
            SettingsItem("Contactar Suporte", Icons.Default.SupportAgent) {
                navController.navigate("support")
            }
        }
    }
}

// --- COMPONENTES ---
@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title, 
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            content()
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector, enabled: Boolean = true, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = enabled) { onClick() }
    ) {
        Icon(
            icon, 
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun SwitchItem(title: String, icon: ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            icon, 
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked, 
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )
    }
}
