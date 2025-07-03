package com.example.projeto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Data class for user account information
data class UserAccount(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val role: UserRole,
    val lastLogin: String,
    val isActive: Boolean = true
)

@Composable
fun SettingsContent(paddingValues: PaddingValues) {
    // Exemplo de implementação básica da tela de configurações
    Column(modifier = Modifier.padding(paddingValues)) {
        Text(text = "Configurações", style = MaterialTheme.typography.headlineLarge)

        // Exemplo de exibição de algumas configurações
        Text(text = "Notificações Push: Ativadas")
        Text(text = "Tema Escuro: Ativado")
        Text(text = "Idioma: Português")
        Text(text = "Qualidade de Vídeo: HD")

        // Adicione mais componentes de UI conforme necessário
    }
}

// Enum for user roles
enum class UserRole {
    ADMIN, USER, VIEWER
}

// Data class for notification settings
data class NotificationSettings(
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val alertTypes: Set<String> = setOf("MOVEMENT", "SYSTEM", "SECURITY")
)

// Data class for display settings
data class DisplaySettings(
    val darkTheme: Boolean = true,
    val language: String = "pt",
    val fontSize: FontSize = FontSize.MEDIUM,
    val autoRefresh: Boolean = true,
    val refreshInterval: Int = 30 // seconds
)

// Enum for font sizes
enum class FontSize {
    SMALL, MEDIUM, LARGE
}

// Data class for security settings
data class SecuritySettings(
    val autoLockEnabled: Boolean = true,
    val autoLockTime: Int = 5, // minutes
    val biometricEnabled: Boolean = false,
    val twoFactorEnabled: Boolean = false,
    val sessionTimeout: Int = 60 // minutes
)

// Data class for camera settings
data class CameraSettings(
    val defaultQuality: VideoQuality = VideoQuality.HD,
    val autoRecord: Boolean = false,
    val motionDetection: Boolean = true,
    val nightVision: Boolean = true,
    val storageLocation: String = "local"
)

// Enum for video quality
enum class VideoQuality {
    LOW, MEDIUM, HD, FULL_HD
}

// Main Settings class to manage all app settings
class Settings {
    
    // Current user account
    private var currentUser: UserAccount? = null
    
    // Settings objects
    private var notificationSettings = NotificationSettings()
    private var displaySettings = DisplaySettings()
    private var securitySettings = SecuritySettings()
    private var cameraSettings = CameraSettings()
    
    // App version and info
    private val appVersion = "1.0.0"
    private val buildNumber = "100"
    
    init {
        // Initialize with default user
        loadDefaultUser()
    }
    
    // User Account Management
    fun getCurrentUser(): UserAccount? = currentUser
    
    fun updateUserEmail(newEmail: String): Boolean {
        return currentUser?.let { user ->
            currentUser = user.copy(email = newEmail)
            true
        } ?: false
    }
    
    fun updateUserPassword(oldPassword: String, newPassword: String): Boolean {
        // In a real app, you would validate the old password
        // and hash the new password before storing
        return currentUser != null
    }
    
    fun logout(): Boolean {
        currentUser = null
        return true
    }
    
    fun isUserLoggedIn(): Boolean = currentUser != null
    
    fun getUserRole(): UserRole? = currentUser?.role
    
    // Notification Settings
    fun getNotificationSettings(): NotificationSettings = notificationSettings
    
    fun updateNotificationSettings(settings: NotificationSettings) {
        notificationSettings = settings
    }
    
    fun togglePushNotifications(): Boolean {
        notificationSettings = notificationSettings.copy(
            pushNotifications = !notificationSettings.pushNotifications
        )
        return notificationSettings.pushNotifications
    }
    
    fun toggleEmailNotifications(): Boolean {
        notificationSettings = notificationSettings.copy(
            emailNotifications = !notificationSettings.emailNotifications
        )
        return notificationSettings.emailNotifications
    }
    
    fun toggleSound(): Boolean {
        notificationSettings = notificationSettings.copy(
            soundEnabled = !notificationSettings.soundEnabled
        )
        return notificationSettings.soundEnabled
    }
    
    // Display Settings
    fun getDisplaySettings(): DisplaySettings = displaySettings
    
    fun updateDisplaySettings(settings: DisplaySettings) {
        displaySettings = settings
    }
    
    fun toggleDarkTheme(): Boolean {
        displaySettings = displaySettings.copy(
            darkTheme = !displaySettings.darkTheme
        )
        return displaySettings.darkTheme
    }
    
    fun setLanguage(language: String) {
        displaySettings = displaySettings.copy(language = language)
    }
    
    fun setFontSize(fontSize: FontSize) {
        displaySettings = displaySettings.copy(fontSize = fontSize)
    }
    
    fun setRefreshInterval(seconds: Int) {
        displaySettings = displaySettings.copy(refreshInterval = seconds)
    }
    
    // Security Settings
    fun getSecuritySettings(): SecuritySettings = securitySettings
    
    fun updateSecuritySettings(settings: SecuritySettings) {
        securitySettings = settings
    }
    
    fun toggleAutoLock(): Boolean {
        securitySettings = securitySettings.copy(
            autoLockEnabled = !securitySettings.autoLockEnabled
        )
        return securitySettings.autoLockEnabled
    }
    
    fun setAutoLockTime(minutes: Int) {
        securitySettings = securitySettings.copy(autoLockTime = minutes)
    }
    
    fun toggleBiometric(): Boolean {
        securitySettings = securitySettings.copy(
            biometricEnabled = !securitySettings.biometricEnabled
        )
        return securitySettings.biometricEnabled
    }
    
    fun toggleTwoFactor(): Boolean {
        securitySettings = securitySettings.copy(
            twoFactorEnabled = !securitySettings.twoFactorEnabled
        )
        return securitySettings.twoFactorEnabled
    }
    
    // Camera Settings
    fun getCameraSettings(): CameraSettings = cameraSettings
    
    fun updateCameraSettings(settings: CameraSettings) {
        cameraSettings = settings
    }
    
    fun setVideoQuality(quality: VideoQuality) {
        cameraSettings = cameraSettings.copy(defaultQuality = quality)
    }
    
    fun toggleAutoRecord(): Boolean {
        cameraSettings = cameraSettings.copy(
            autoRecord = !cameraSettings.autoRecord
        )
        return cameraSettings.autoRecord
    }
    
    fun toggleMotionDetection(): Boolean {
        cameraSettings = cameraSettings.copy(
            motionDetection = !cameraSettings.motionDetection
        )
        return cameraSettings.motionDetection
    }
    
    fun toggleNightVision(): Boolean {
        cameraSettings = cameraSettings.copy(
            nightVision = !cameraSettings.nightVision
        )
        return cameraSettings.nightVision
    }
    
    // App Information
    fun getAppVersion(): String = appVersion
    fun getBuildNumber(): String = buildNumber
    fun getAppInfo(): String = "Versão $appVersion (Build $buildNumber)"
    
    // Reset Settings
    fun resetToDefaults() {
        notificationSettings = NotificationSettings()
        displaySettings = DisplaySettings()
        securitySettings = SecuritySettings()
        cameraSettings = CameraSettings()
    }
    
    fun resetNotificationSettings() {
        notificationSettings = NotificationSettings()
    }
    
    fun resetDisplaySettings() {
        displaySettings = DisplaySettings()
    }
    
    fun resetSecuritySettings() {
        securitySettings = SecuritySettings()
    }
    
    fun resetCameraSettings() {
        cameraSettings = CameraSettings()
    }
    
    // Export/Import Settings (for backup)
    fun exportSettings(): Map<String, Any> {
        return mapOf(
            "notifications" to notificationSettings,
            "display" to displaySettings,
            "security" to securitySettings,
            "camera" to cameraSettings
        )
    }
    
    // Support and Help
    fun getSupportEmail(): String = "suporte@seguranca.com"
    fun getSupportPhone(): String = "+351 123 456 789"
    fun getHelpUrl(): String = "https://help.seguranca.com"
    
    // Private helper function
    private fun loadDefaultUser() {
        currentUser = UserAccount(
            id = 1,
            username = "admin",
            email = "admin@seguranca.com",
            fullName = "Administrador",
            role = UserRole.ADMIN,
            lastLogin = "Hoje às 09:00",
            isActive = true
        )
    }
}