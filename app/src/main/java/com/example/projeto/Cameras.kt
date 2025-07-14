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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.ImageLoader
import com.example.projeto.viewmodel.UserViewModel

/**
 * 📹 SISTEMA DE CÂMARAS POR UTILIZADOR
 * 
 * Cada utilizador tem câmaras específicas baseadas no seu perfil:
 * - OsmarG: Segurança residencial (Sala, Quarto, Quintal, etc.)
 * - DiogoS: Segurança comercial (Estacionamento, Receção, etc.)
 * - Admin: Monitorização geral do sistema
 */

// ==================== ENUMS E DATA CLASSES ====================

/** Estados possíveis das câmaras */
enum class CameraStatus {
    ONLINE, OFFLINE, MAINTENANCE, ERROR
}

/** Dados de uma câmara individual */
data class Camera(
    val id: Int,
    val name: String,
    val location: String,
    val status: CameraStatus,
    val ipAddress: String,
    val isRecording: Boolean = false,
    val batteryLevel: Int? = null,
    val lastActivity: String,
    val userId: Int // 🆕 Associa câmara ao utilizador
)

// ==================== COMPONENTE PRINCIPAL ====================

@Composable
fun CameraContent(paddingValues: PaddingValues = PaddingValues()) {
    // ViewModels
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()
    
    // Estados locais
    var selectedCamera by remember { mutableStateOf<Camera?>(null) }
    var userCameras by remember { mutableStateOf<List<Camera>>(emptyList()) }
    
    // 🎯 Carrega câmaras específicas do utilizador quando muda
    LaunchedEffect(currentUser) {
        val userId = currentUser?.userId ?: 1 // Default OsmarG
        userCameras = getCamerasForUser(userId)
    }

    // ==================== CÂMARA EXPANDIDA ====================
    
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

    // ==================== CABEÇALHO ====================
    
    Column(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp)
    ) {
        // Ícone da câmara
        Icon(
            painter = painterResource(R.drawable.videocam),
            contentDescription = "camera",
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth()
                .size(35.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        // Título
        Text(
            text = "Câmaras",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 20.dp)
        )
        
        // 🆕 Mostra o utilizador atual e o número de câmaras
        currentUser?.let { user ->
            Text(
                text = "Utilizador: ${user.username} | ${userCameras.size} câmaras",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp)
            )
        }
    }

    // ==================== Grelha das câmaras ====================
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .padding(top = 200.dp)
    ) {
        if (userCameras.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .height((userCameras.size / 2 + 1) * 270.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(userCameras) { camera ->
                    CameraCard(camera = camera, onClick = { selectedCamera = it })
                }
            }
        } else {
            // Estado vazio
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhuma câmara disponível para este utilizador",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ==================== Cartão da câmara ====================

@Composable
fun CameraCard(camera: Camera, onClick: (Camera) -> Unit) {
    val context = LocalContext.current

    // Seleciona o GIF baseado na localização
    val gifRes = when (camera.location) {
        "Sala" -> R.drawable.sala
        "Quarto" -> R.drawable.quarto
        "Estacionamento" -> R.drawable.estacionamento
        "Cozinha" -> R.drawable.cozinha
        "Quintal" -> R.drawable.quintal
        "Porta_Entrada" -> R.drawable.cao_entrada
        "Rececao" -> R.drawable.rececao
        "Armazem" -> R.drawable.armazem
        "Sala_Reunioes" -> R.drawable.sala_reunioes
        "Patio_Exterior" -> R.drawable.patio_exterior
        "Estacionamento_Carros" -> R.drawable.carros_estacionamento
        "Porta_Principal" -> R.drawable.porta_principal
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
            // Vídeo da câmara
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

            // Nome da câmara
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

// ==================== Vista do ecrâ Expandido ====================

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
        "Rececao" -> R.drawable.rececao
        "Armazem" -> R.drawable.armazem
        "Sala_Reunioes" -> R.drawable.sala_reunioes
        "Patio_Exterior" -> R.drawable.patio_exterior
        "Estacionamento_Carros" -> R.drawable.carros_estacionamento
        "Porta_Principal" -> R.drawable.porta_principal
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

            // Nome da câmara
            Text(
                text = camera.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)

            )

            // Vídeo expandido
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = Color.Black.copy(alpha = 0.2f),
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

            // Informações da câmara
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
                        append("\n")
                        append("🔧 Estado: ${camera.status}\n")
                        camera.batteryLevel?.let {
                            append("\n🔋 Bateria: $it%")
                        }
                        append("\n👤 Utilizador ID: ${camera.userId}")
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Botão fechar
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

// ==================== CÂMARAS POR UTILIZADOR ====================

/**
 * 🎯 FUNÇÃO PRINCIPAL - Atribui câmaras específicas para cada utilizador
 * 
 * Cada utilizador tem um perfil diferente de câmaras baseado nas suas necessidades:
 * - Residencial vs Comercial vs Administrativo
 */
fun getCamerasForUser(userId: Int): List<Camera> {
    return when (userId) {
        1 -> getCamerasOsmarG()      // Segurança residencial
        2 -> getCamerasDiogoS()      // Segurança comercial  
        3 -> getCamerasAdmin()       // Monitorização geral
        else -> getCamerasDefault()  // Câmaras genéricas
    }
}

/**
 * 🏠 CÂMARAS PARA o utilizador OsmarGonçalves - Segurança Residencial
 * Foco em monitorização doméstica e familiar
 */
private fun getCamerasOsmarG(): List<Camera> = listOf(
    Camera(1, "Entrada", "Porta_Entrada", CameraStatus.ONLINE, "192.168.1.101", true, null, "10:30", 1),
    Camera(2, "Sala", "Sala", CameraStatus.ONLINE, "192.168.1.102", false, null, "09:45", 1),
    Camera(3, "Quarto", "Quarto", CameraStatus.ONLINE, "192.168.1.103", false, 15, "08:45", 1),
    Camera(4, "Cozinha", "Cozinha", CameraStatus.OFFLINE, "192.168.1.104", false, null, "07:20", 1),
    Camera(5, "Quintal", "Quintal", CameraStatus.ONLINE, "192.168.1.105", true, 85, "07:15", 1)
)

/**
 * 🏢 CÂMARAS PARA o Utilizador DiogoSilva - Segurança Comercial
 * Foco em monitorização empresarial e controlo de acesso
 */
private fun getCamerasDiogoS(): List<Camera> = listOf(
    Camera(6, "Receção", "Rececao", CameraStatus.ONLINE, "192.168.2.101", true, null, "11:20", 2),
    Camera(7, "Estacionamento", "Estacionamento_Carros", CameraStatus.ONLINE, "192.168.2.102", true, null, "10:15", 2),
    Camera(8, "Armazém", "Armazem", CameraStatus.MAINTENANCE, "192.168.2.103", false, 45, "09:30", 2),
    Camera(9, "Porta Principal", "Porta_Principal", CameraStatus.ONLINE, "192.168.2.104", true, null, "08:45", 2),
    Camera(10, "Sala Reuniões", "Sala_Reunioes", CameraStatus.ONLINE, "192.168.2.105", false, 78, "07:50", 2),
    Camera(11, "Pátio Exterior", "Patio_Exterior", CameraStatus.OFFLINE, "192.168.2.106", false, null, "06:30", 2)
)

/**
 * 👨‍💼 CÂMARAS para o administrador - Monitorização Geral
 * Acesso a todas as câmaras do sistema
 */
private fun getCamerasAdmin(): List<Camera> = listOf(
    Camera(1, "Entrada", "Porta_Entrada", CameraStatus.ONLINE, "192.168.1.102", false, null, "09:45", 3),
    Camera(2, "Sala", "Sala", CameraStatus.ONLINE, "192.168.1.103", false, 15, "08:45", 3),
    Camera(3, "Quarto", "Quarto", CameraStatus.OFFLINE, "192.168.1.104", false, 12, "07:20", 3),
    Camera(4, "Cozinha", "Cozinha", CameraStatus.ERROR, "192.168.1.105", false, null, "06:45", 3),
    Camera(5, "Quintal", "Quintal", CameraStatus.ONLINE, "192.168.1.106", true, 85, "06:30", 3),
    Camera(6, "Receção", "Rececao", CameraStatus.ONLINE, "192.168.2.101", true, null, "11:20", 3),
    Camera(7, "Estacionamento", "Estacionamento_Carros", CameraStatus.ONLINE, "192.168.2.102", true, null, "10:15", 3),
    Camera(8, "Armazém", "Armazem", CameraStatus.MAINTENANCE, "192.168.2.103", false, 45, "09:30", 3),
    Camera(9, "Porta Principal", "Porta_Principal", CameraStatus.ONLINE, "192.168.2.104", true, null, "08:45", 3),
    Camera(10, "Sala Reuniões", "Sala_Reunioes", CameraStatus.ONLINE, "192.168.2.105", false, 78, "07:50", 3),
    Camera(11, "Pátio Exterior", "Patio_Exterior", CameraStatus.OFFLINE, "192.168.2.106", false, null, "06:30", 3)
)

/**
 * 🔧 CÂMARAS PADRÃO - Para outros utilizadores
 * Conjunto básico de câmaras genéricas
 */
private fun getCamerasDefault(): List<Camera> = listOf(
    Camera(16, "Genérica 01", "Sala", CameraStatus.ONLINE, "192.168.9.101", false, null, "10:00", 0),
    Camera(17, "Genérica 02", "Porta_Entrada", CameraStatus.ONLINE, "192.168.9.102", false, 50, "09:30", 0)
)