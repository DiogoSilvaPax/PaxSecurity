package com.example.projeto.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeManager : ViewModel() {
    var isDarkTheme by mutableStateOf(false)
        private set
    
    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
    
    fun setDarkTheme(isDark: Boolean) {
        isDarkTheme = isDark
    }
}