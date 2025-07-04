package com.example.projeto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class for individual camera
data class Camera(
    val id: Int,
    val name: String,
    val location: String,
    val status: CameraStatus,
    val ipAddress: String,
    val isRecording: Boolean = false,
    val batteryLevel: Int? = null, // null for wired cameras
    val lastActivity: String
)
@Composable
fun CameraContent(paddingValues: PaddingValues) {

    Column(modifier = Modifier.padding(paddingValues)) {

        Icon(
            painter = painterResource(R.drawable.videocam),
            contentDescription = "camera",
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth()
                .size(35.dp),
            tint = Color.White

        )


        Text(
            text = "Câmaras",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)

        )

    }









    // Obtenha a lista de câmeras
    val cameras = Cameras().getAllCameras()

    // Verifique se há câmeras para exibir
    if (cameras.isNotEmpty()) {
        Column(modifier = Modifier.padding(paddingValues)) {
            cameras.forEach { camera ->
                Text(text = "Nome: ${camera.name}, Localização: ${camera.location}, Status: ${camera.status}")
            }
        }
    } else {
        // Mensagem caso não haja câmeras
        Text(text = "Nenhuma câmera disponível", modifier = Modifier.padding(paddingValues))
    }
}

// Enum for camera status
enum class CameraStatus {
    ONLINE, OFFLINE, MAINTENANCE, ERROR
}

// Main Cameras class to manage all cameras
class Cameras {
    
    // List of all cameras in the system
    private val cameraList = mutableListOf<Camera>()
    
    init {
        // Initialize with sample cameras
        loadSampleCameras()
    }
    
    // Get all cameras
    fun getAllCameras(): List<Camera> {
        return cameraList.toList()
    }
    
    // Get camera by ID
    fun getCameraById(id: Int): Camera? {
        return cameraList.find { it.id == id }
    }
    
    // Get cameras by status
    fun getCamerasByStatus(status: CameraStatus): List<Camera> {
        return cameraList.filter { it.status == status }
    }
    
    // Get online cameras count
    fun getOnlineCamerasCount(): Int {
        return cameraList.count { it.status == CameraStatus.ONLINE }
    }
    
    // Get total cameras count
    fun getTotalCamerasCount(): Int {
        return cameraList.size
    }
    
    // Add new camera
    fun addCamera(camera: Camera): Boolean {
        return if (cameraList.none { it.id == camera.id }) {
            cameraList.add(camera)
            true
        } else {
            false // Camera with this ID already exists
        }
    }
    
    // Remove camera
    fun removeCamera(id: Int): Boolean {
        return cameraList.removeIf { it.id == id }
    }
    
    // Update camera status
    fun updateCameraStatus(id: Int, status: CameraStatus): Boolean {
        val camera = getCameraById(id)
        return if (camera != null) {
            val index = cameraList.indexOf(camera)
            cameraList[index] = camera.copy(status = status)
            true
        } else {
            false
        }
    }
    
    // Start recording on camera
    fun startRecording(id: Int): Boolean {
        val camera = getCameraById(id)
        return if (camera != null && camera.status == CameraStatus.ONLINE) {
            val index = cameraList.indexOf(camera)
            cameraList[index] = camera.copy(isRecording = true)
            true
        } else {
            false
        }
    }
    
    // Stop recording on camera
    fun stopRecording(id: Int): Boolean {
        val camera = getCameraById(id)
        return if (camera != null) {
            val index = cameraList.indexOf(camera)
            cameraList[index] = camera.copy(isRecording = false)
            true
        } else {
            false
        }
    }
    
    // Get cameras with low battery (below 20%)
    fun getLowBatteryCameras(): List<Camera> {
        return cameraList.filter { 
            it.batteryLevel != null && it.batteryLevel < 20 
        }
    }
    
    // Check if any camera is recording
    fun isAnyRecording(): Boolean {
        return cameraList.any { it.isRecording }
    }
    
    // Get recording cameras
    fun getRecordingCameras(): List<Camera> {
        return cameraList.filter { it.isRecording }
    }
    
    // Private function to load sample cameras
    private fun loadSampleCameras() {
        cameraList.addAll(listOf(
            Camera(
                id = 1,
                name = "Câmara 01",
                location = "Entrada Principal",
                status = CameraStatus.ONLINE,
                ipAddress = "192.168.1.101",
                isRecording = true,
                batteryLevel = null,
                lastActivity = "10:30"
            ),
            Camera(
                id = 2,
                name = "Câmara 02",
                location = "Sala de Estar",
                status = CameraStatus.ONLINE,
                ipAddress = "192.168.1.102",
                isRecording = false,
                batteryLevel = null,
                lastActivity = "09:45"
            ),
            Camera(
                id = 3,
                name = "Câmara 03",
                location = "Jardim",
                status = CameraStatus.ONLINE,
                ipAddress = "192.168.1.103",
                isRecording = false,
                batteryLevel = 15,
                lastActivity = "08:45"
            ),
            Camera(
                id = 4,
                name = "Câmara 04",
                location = "Cozinha",
                status = CameraStatus.OFFLINE,
                ipAddress = "192.168.1.104",
                isRecording = false,
                batteryLevel = null,
                lastActivity = "07:20"
            ),
            Camera(
                id = 5,
                name = "Câmara 05",
                location = "Garagem",
                status = CameraStatus.ONLINE,
                ipAddress = "192.168.1.105",
                isRecording = true,
                batteryLevel = 85,
                lastActivity = "07:15"
            ),
            Camera(
                id = 6,
                name = "Câmara 06",
                location = "Quarto Principal",
                status = CameraStatus.MAINTENANCE,
                ipAddress = "192.168.1.106",
                isRecording = false,
                batteryLevel = null,
                lastActivity = "06:30"
            )
        ))
    }
}