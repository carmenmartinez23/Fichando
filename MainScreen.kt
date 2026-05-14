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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class RangoVacaciones(val inicio: Int, val fin: Int, val mes: Int)
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
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(20.dp))
        // 3. Sección Horario
        Text(
            text = "Horario",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        CalendarOficial()

        // 4. Sección Chat
        var mensaje by remember { mutableStateOf("") }
        var mensajes by remember { mutableStateOf(listOf("AJ: Llegaré 5 minutos tarde", "MH: Mañana necesito ir al médico")) }

        val listState = rememberLazyListState()

// Scroll automático al último mensaje
        LaunchedEffect(mensajes.size) {
            listState.animateScrollToItem(mensajes.size - 1)
        }
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Chat",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
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
                            isOwn = partes[0] == "Yo",
                            modifier = Modifier
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

    // --- ESTADOS PARA EL CALENDARIO ---
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    // --- ESTADOS DE ALERTAS ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column {
                // Fila de navegación de meses
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFE1F5FE)).padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF1A6699))
                    }
                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A6699)
                    )
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF1A6699))
                    }
                }

                // LLAMADA AL NUEVO CALENDARIO
                CalendarioSemanal(currentMonth = currentMonth)
            }
        }
    }
}
@Composable
fun CalendarioSemanal(
    currentMonth: YearMonth,
    vacaciones: List<RangoVacaciones> = listOf(RangoVacaciones(2, 8, 6))
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    // Obtenemos el día de la semana del día 1 (1=Lunes, 7=Domingo)
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value
    val diasSemana = listOf("L", "M", "X", "J", "V", "S", "D")

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // 1. Cabecera L, M, X...
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            diasSemana.forEach { dia ->
                Text(
                    text = dia,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Cálculo de la cuadrícula
        // 'offset' son los huecos vacíos antes del día 1
        val totalCells = daysInMonth + (firstDayOfMonth - 1)
        val rows = (totalCells + 6) / 7 // Calculamos cuántas filas necesitamos

        repeat(rows) { rowIndex ->
            Row(modifier = Modifier.fillMaxWidth().height(60.dp)) { // Altura ajustada para que quepa todo
                repeat(7) { columnIndex ->
                    val cellIndex = rowIndex * 7 + columnIndex
                    val dayNumber = cellIndex - (firstDayOfMonth - 2)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .border(0.5.dp, Color.LightGray)
                    ) {
                        if (dayNumber in 1..daysInMonth) {
                            // Lógica de contenido para días válidos
                            val esVacaciones = vacaciones.any {
                                dayNumber in it.inicio..it.fin && currentMonth.monthValue == it.mes
                            }
                            val esDiaLaborable = (columnIndex + 1) in 1..5 // Lunes a Viernes

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$dayNumber",
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(2.dp),
                                    fontWeight = if (esVacaciones) FontWeight.Bold else FontWeight.Normal
                                )

                                if (esVacaciones) {
                                    // BLOQUE NARANJA VACACIONES
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 2.dp)
                                            .background(Color(0xFFFFB74D), RoundedCornerShape(2.dp))
                                            .padding(2.dp)
                                    ) {
                                        Text("VAC", color = Color.White, fontSize = 9.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                    }
                                } else if (esDiaLaborable) {
                                    // BLOQUE AZUL TRABAJO
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 2.dp)
                                            .background(Color(0xFF5DADE2), RoundedCornerShape(2.dp))
                                            .padding(2.dp)
                                    ) {
                                        Text("8:00\n15:00", color = Color.White, fontSize = 9.sp, lineHeight = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ChatBubble(user: String, message: String, isOwn: Boolean = false, modifier: Modifier) {
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
                fontSize = 16.sp,
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