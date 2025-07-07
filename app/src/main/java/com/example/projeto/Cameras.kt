package com.example.projeto

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.ImageLoader

// ENUM PARA ESTADO DA CÂMARA
enum class CameraStatus {
    ONLINE, OFFLINE, MAINTENANCE, ERROR
}

// DADOS DE UMA CÂMARA
data class Camera(
    val id: Int,
    val name: String,
    val location: String,
    val status: CameraStatus,
    val ipAddress: String,
    val isRecording: Boolean = false,
    val batteryLevel: Int? = null,
    val lastActivity: String
)

@Composable
fun CameraContent(paddingValues: PaddingValues = PaddingValues()) {
    val cameras = getSampleCameras()
    var selectedCamera by remember { mutableStateOf<Camera?>(null) }

    selectedCamera?.let { camera ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f)),
            contentAlignment = Alignment.Center
        ) {
            ExpandedCameraView(camera = camera, onClose = { selectedCamera = null })
        }
        return
    }

    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.videocam),
            contentDescription = "camera",
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth()
                .size(35.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Câmaras",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 100.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(top = 150.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .height((cameras.size / 2 + 1) * 270.dp),
            contentPadding = PaddingValues(8.dp)

        ) {
            items(cameras) { camera ->
                CameraCard(camera = camera, onClick = { selectedCamera = it })
            }
        }
    }
}

@Composable
fun CameraCard(camera: Camera, onClick: (Camera) -> Unit) {
    val context = LocalContext.current

    val gifRes = when (camera.location) {
        "Sala" -> R.drawable.sala
        "Quarto" -> R.drawable.quarto
        "Estacionamento" -> R.drawable.estacionamento
        "Cozinha" -> R.drawable.cozinha
        "Quintal" -> R.drawable.quintal
        "Porta_Entrada" -> R.drawable.cao_entrada
        else -> R.drawable.quarto
    }

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick(camera) }
            .padding(top = 50.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(gifRes)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Vídeo da ${camera.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            )

            Text(
                text = camera.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun ExpandedCameraView(camera: Camera, onClose: () -> Unit) {
    val context = LocalContext.current

    val gifRes = when (camera.location) {
        "Sala" -> R.drawable.sala
        "Quarto" -> R.drawable.quarto
        "Estacionamento" -> R.drawable.estacionamento
        "Cozinha" -> R.drawable.cozinha
        "Quintal" -> R.drawable.quintal
        "Porta_Entrada" -> R.drawable.cao_entrada
        else -> R.drawable.quarto
    }

    val imageLoader = ImageLoader.Builder(context)
        .components { add(GifDecoder.Factory()) }
        .build()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Text(
                text = camera.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 30.dp)
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = Color.Black.copy(alpha = 0.2f), // leve fundo escuro
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(gifRes).build(),
                    imageLoader = imageLoader,
                    contentDescription = "Vídeo expandido de ${camera.name}",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = buildString {
                        append("📍 Localização: ${camera.location}\n")
                        append("\n")
                        append("🌐 IP: ${camera.ipAddress}\n")
                        append("\n") // pequeno espaçamento visual
                        append("🔧 Estado: ${camera.status}\n")
                        camera.batteryLevel?.let {
                            append("\n🔋 Bateria: $it%")
                        }
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}



fun getSampleCameras(): List<Camera> = listOf(
    Camera(1, "Câmara 01", "Porta_Entrada", CameraStatus.ONLINE, "192.168.1.101", true, null, "10:30"),
    Camera(2, "Câmara 02", "Sala", CameraStatus.ONLINE, "192.168.1.102", false, null, "09:45"),
    Camera(3, "Câmara 03", "Quarto", CameraStatus.ONLINE, "192.168.1.103", false, 15, "08:45"),
    Camera(4, "Câmara 04", "Cozinha", CameraStatus.OFFLINE, "192.168.1.104", false, null, "07:20"),
    Camera(5, "Câmara 05", "Quintal", CameraStatus.ONLINE, "192.168.1.105", true, 85, "07:15"),
    Camera(6, "Câmara 06", "Estacionamento", CameraStatus.MAINTENANCE, "192.168.1.106", false, null, "06:30")
)