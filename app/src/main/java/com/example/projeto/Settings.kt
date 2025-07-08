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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Conta Section
            SectionTitle("Conta")
            SimpleSettingsItem("Alterar palavra-passe", Icons.Default.Person) {
                navController.navigate("change_password")
            }
            SimpleSettingsItem("Alterar email", Icons.Default.Email) {
                navController.navigate("change_email")
            }
            SimpleSettingsItem("Terminar Sessão", Icons.Default.Logout) {
                navController.navigate("logout")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Notificações Section
            SectionTitle("Notificações")
            SimpleSwitchItem(
                title = "Ativar notificações",
                icon = Icons.Default.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Visualização Section
            SectionTitle("Visualização")
            SimpleSwitchItem(
                title = "Tema Escuro",
                icon = Icons.Default.DarkMode,
                checked = themeManager.isDarkTheme,
                onCheckedChange = { 
                    themeManager.DarkTheme(it)
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sobre Section
            SectionTitle("Sobre")
            SimpleSettingsItem("Versão 1.0.0", Icons.Default.Info, enabled = false, showArrow = false)
            SimpleSettingsItem("Contactar Suporte", Icons.Default.SupportAgent) {
                navController.navigate("support")
            }
        }
    }
}

// --- NOVOS COMPONENTES SIMPLIFICADOS ---
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun SimpleSettingsItem(
    title: String, 
    icon: ImageVector, 
    enabled: Boolean = true, 
    showArrow: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(enabled = enabled) { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(20.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.weight(1f),
            color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        
        if (showArrow && enabled) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SimpleSwitchItem(
    title: String, 
    icon: ImageVector, 
    checked: Boolean, 
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(20.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
