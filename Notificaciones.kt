package com.example.fichando

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.CoroutineScope

data class Notificacion(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    var isRead: Boolean = false
)
@Composable
fun NotificacionScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onDeleteAccount: () -> Unit
) {
    val notificaciones = remember {
        mutableStateListOf(
            Notificacion(1, "Comunicación urgente", "There are many variations of passages of Lorem Ipsum available..."),
            Notificacion(2, "Curso de Prevención Laboral", "Recordatorio: El curso vence el próximo viernes.")
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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

        Divider(thickness = 2.dp, color = Color.LightGray)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notificaciones) { noti ->
                NotificacionItem(noti)
            }
        }
    }
}

@Composable
fun NotificacionItem(notificacion: Notificacion) {
    var expanded by remember { mutableStateOf(false) }
    var leida by remember { mutableStateOf(notificacion.isRead) }

    // El color de fondo cambia según si está abierta y no leída
    // Como en la Captura 005904, el naranja es para las NO leídas
    val backgroundColor = if (!leida) Color(0xFFFFCCBC) else Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable {
                expanded = !expanded
                if (!leida) {
                    leida = true
                    notificacion.isRead = true // Guardamos el estado en el objeto
                }
            }
    ) {
        // --- FILA PRINCIPAL (Lo que se ve siempre) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                painter = painterResource(id = if (leida) R.drawable.abierto else R.drawable.cerrado),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = notificacion.titulo,
                fontSize = 16.sp,
                fontWeight = if (!leida) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black
            )
        }

        // --- CONTENIDO EXPANDIBLE ---
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // El interior siempre es blanco al abrirse
                    .padding(horizontal = 5.dp, vertical = 10.dp)

            ) {
                Text(
                    text = notificacion.mensaje,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Divider(color = Color.LightGray, thickness = 0.5.dp)
    }
}