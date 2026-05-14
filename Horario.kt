package com.example.fichando

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorarioScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    profileImage: Uri?,
    onDeleteAccount: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // --- ESTADOS PARA EL CALENDARIO ---
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    // --- ESTADOS PARA PEDIR VACACIONES ---
    var diaIni by remember { mutableStateOf("") }
    var mesIni by remember { mutableStateOf("") }
    var añoIni by remember { mutableStateOf("") }
    var diaFin by remember { mutableStateOf("") }
    var mesFin by remember { mutableStateOf("") }
    var añoFin by remember { mutableStateOf("") }

    // --- ESTADO CAMBIAR VACACIONES (TEXTO LARGO) ---
    var cambioTexto by remember { mutableStateOf("") }

    // --- ESTADOS DE ALERTAS ---
    var showAlertaPedir by remember { mutableStateOf(false) }
    var showAlertaCambio by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

        Spacer(modifier = Modifier.height(20.dp))
        Text("Horario", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        // --- CALENDARIO CON BOTONES DE NAVEGACIÓN ---
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

        Spacer(modifier = Modifier.height(30.dp))

        // --- SECCIÓN: PEDIR VACACIONES ---
        Text("Pedir vacaciones:", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    InputVacaciones("Dia", diaIni) { diaIni = it }
                    InputVacaciones("Mes", mesIni) { mesIni = it }
                    InputVacaciones("Año", añoIni, width = 80.dp) { añoIni = it }
                }
                Row {
                    InputVacaciones("Dia", diaFin) { diaFin = it }
                    InputVacaciones("Mes", mesFin) { mesFin = it }
                    InputVacaciones("Año", añoFin, width = 80.dp) { añoFin = it }
                }
            }
            Button(
                onClick = { showAlertaPedir = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5DADE2)),
                modifier = Modifier.height(50.dp)
            ) {
                Text("Pedir")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- SECCIÓN: PRÓXIMAS VACACIONES (TABLA) ---
        Text("Próximas vacaciones:", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        TableVacaciones()

        Spacer(modifier = Modifier.height(30.dp))

        // --- SECCIÓN: CAMBIAR VACACIONES (TEXTO LARGO) ---
        Text("Cambiar vacaciones:", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = cambioTexto,
            onValueChange = { cambioTexto = it },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            placeholder = { Text("Escribe aquí los motivos del cambio...") },
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                showAlertaCambio = true
                cambioTexto = "" // Borrar texto automáticamente
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5DADE2))
        ) {
            Text("Pedir")
        }
    }

    // --- ALERTAS ---
    if (showAlertaPedir) {
        AlertDialog(
            onDismissRequest = { showAlertaPedir = false },
            confirmButton = {
                TextButton(onClick = { showAlertaPedir = false }) { Text("Aceptar") }
            },
            title = { Text("Estado") },
            text = { Text("Solicitud de vacaciones enviada") }
        )
    }

    if (showAlertaCambio) {
        AlertDialog(
            onDismissRequest = { showAlertaCambio = false },
            confirmButton = {
                TextButton(onClick = { showAlertaCambio = false }) { Text("Aceptar") }
            },
            title = { Text("Estado") },
            text = { Text("Solicitud enviada") }
        )
    }
}

@Composable
fun InputVacaciones(
    label: String,
    value: String,
    width: Dp = 60.dp,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(4.dp)) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(width).height(50.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}

@Composable
fun TableVacaciones() {
    Column(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)) {
        Row(modifier = Modifier.background(Color(0xFF006699)).fillMaxWidth()) {
            Text("Comienzo", Modifier.weight(1f).padding(8.dp), color = Color.White, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.width(1.dp).height(35.dp), color = Color.White)
            Text("Finalización", Modifier.weight(1f).padding(8.dp), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("02/06/2026", Modifier.weight(1f).padding(8.dp))
            Divider(modifier = Modifier.width(1.dp).height(35.dp), color = Color.Gray)
            Text("08/06/2026", Modifier.weight(1f).padding(8.dp))
        }
        Divider(color = Color.Gray)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("-", Modifier.weight(1f).padding(8.dp), textAlign = TextAlign.Center)
            Divider(modifier = Modifier.width(1.dp).height(35.dp), color = Color.Gray)
            Text("-", Modifier.weight(1f).padding(8.dp), textAlign = TextAlign.Center)
        }
    }
}