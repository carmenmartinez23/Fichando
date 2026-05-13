package com.example.fichando

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class MenuOption(val icon: ImageVector, val title: String, val subtitle: String)

val menuOptions = listOf(
    MenuOption(Icons.Default.Person, "Perfil", "Datos personales, contraseña."),
    MenuOption(Icons.Default.DateRange, "Horario", "Pedir vacaciones..."),
    MenuOption(Icons.Default.MailOutline, "Chat", ""),
    MenuOption(Icons.Default.FolderOpen, "Documentos", "Nóminas, Contratos, etc."),
    MenuOption(Icons.Default.Notifications, "Notificaciones", "Contacta con nosotros"),
    MenuOption(Icons.Default.Groups, "Sobre nosotros", "Contacta con nosotros"),
    MenuOption(Icons.Default.Help, "Preguntas", "Preguntas y respuestas.")
)

@Composable
fun MainScreen(
    navController: NavController,
    drawerState: DrawerState, // Añadido para controlar el menú global
    scope: CoroutineScope,    // Añadido para controlar el menú global
    globalProfileImage: Uri?,
    onLogout: () -> Unit = {}
) {
        CalendarOficial()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()) // Importante para que no se corte la pantalla

        ) {
            // 1. Barra Superior (Header)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Abrir Menú",
                        Modifier
                            .size(80.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Maria", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Operador general", fontSize = 22.sp, color = Color.Gray)
                }
                Box(contentAlignment = Alignment.CenterEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Perfil",
                        modifier = Modifier
                            .size(70.dp)
                            // 1. Primero recortamos en forma de círculo
                            .clip(CircleShape)
                            // 2. Luego añadimos el borde (después del clip para que se ajuste al borde)
                            .border(2.dp, Color(0xFF00C6FF), CircleShape),
                        // 3. Cambiamos Fit por Crop para que rellene todo el espacio
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Divider(thickness = 1.dp, color = Color.LightGray)
            // 2. Botón Central de Fichaje (Cronómetro)
            // Moví las variables aquí arriba para que funcionen bien
            var isFichado by remember { mutableStateOf(false) }
            var showDialog by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(200.dp)
                        .aspectRatio(1f)
                        .clickable {
                            if (!isFichado) isFichado = true else showDialog = true
                        },
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(
                                id = if (isFichado) R.drawable.pause else R.drawable.play
                            ),
                            contentDescription = "Botón de fichaje",
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
                        // --- VENTANA EMERGENTE (AlertDialog) ---
                        if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(text = "Finalizar turno") },
                            text = { Text(text = "¿Estás segura de que quieres finalizar el turno?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        isFichado = false
                                        showDialog = false
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        // Añadimos el borde aquí:
                                        .border(1.dp, Color.Red, RoundedCornerShape(8.dp))
                                ) {
                                    Text(
                                        "Sí, finalizar",
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp) // Espacio interno para que no pegue al borde
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDialog = false },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        // Añadimos el borde aquí (en gris o azul para que sea más suave):
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                ) {
                                    Text(
                                        "Cancelar",
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        )
                    }

            // 3. Sección Horario (Febrero 2026)
            Text(
                text = "Horario",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color(0xFFBEE3F8), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "< February 2026 >",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold
                    )
                    // Aquí iría la cuadrícula del calendario
                    Spacer(
                        modifier = Modifier.height(100.dp).fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.5f))
                    )
                    Text(
                        "Simulación Calendario",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.LightGray
                    )
                }
            }

            // 4. Sección Chat
            var mensaje by remember { mutableStateOf("") }
            var mensajes by remember { mutableStateOf(listOf("AJ: Llegaré 5 minutos tarde", "MH: Mañana necesito ir al médico")) }

            val listState = rememberLazyListState()

// Scroll automático al último mensaje
            LaunchedEffect(mensajes.size) {
                listState.animateScrollToItem(mensajes.size - 1)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(150.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBEE3F8))
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(mensajes) { msg ->
                        val partes = msg.split(": ", limit = 2)
                        if (partes.size == 2) {
                            ChatBubble(
                                user = partes[0],
                                message = partes[1],
                                isOwn = partes[0] == "Yo"
                            )
                        }
                    }
                }
            }

            // Entrada de mensaje
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var mensaje by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(15.dp)
                )
                IconButton(onClick = {
                    if (mensaje.isNotBlank()) {
                        mensajes = mensajes + "Yo: $mensaje"
                        mensaje = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color(0xFF4299E1))
                }
            }
        }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarOficial() {
    // Estado que controla la fecha seleccionada
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de Jornada",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Este es el calendario oficial de Android Material 3
        DatePicker(
            state = datePickerState,
            showModeToggle = false, // Desactivamos el icono de lápiz para que solo sea calendario
            title = null,           // Quitamos el título interno para usar el nuestro
            headline = null,        // Quitamos la cabecera interna para ahorrar espacio
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Mostrar la fecha seleccionada debajo
        val selectedDate = datePickerState.selectedDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }

        if (selectedDate != null) {
            Text(
                text = "Fecha seleccionada: ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ChatBubble(user: String, message: String, isOwn: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwn) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier.size(20.dp).border(1.dp, Color.Gray, CircleShape)
            ) {
                Text(user, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
            }
        }
        Surface(
            modifier = Modifier.padding(horizontal = 4.dp),
            shape = RoundedCornerShape(10.dp),
            color = if (isOwn) Color(0xFF00C6FF) else Color(0xFF0091FF)
        ) {
            Text(
                message,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(
        navController = navController,
        drawerState = rememberDrawerState(DrawerValue.Closed),
        scope = rememberCoroutineScope(),
        globalProfileImage = null
    )
}
