package com.example.fichando

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.CoroutineScope


data class FAQItem(val id: Int, val pregunta: String, val respuesta: String)

val listaPreguntas = listOf(
    FAQItem(1, "¿La app es gratuita o tiene versión premium?", "Hay solo una versión gratuita."),
    FAQItem(2, "¿Qué permisos necesita la app?", "Ubicación (opcional), notificaciones y almacenamiento, según la configuración de la empresa."),
    FAQItem(3, "¿Cumple con la normativa española de control horario?", "Sí, cumple con el Real Decreto-ley 8/2019 y el RGPD."),
    FAQItem(4, "¿Cómo actualizo la app?", "Desde Google Play o App Store cuando haya nuevas versiones disponibles."),
    FAQItem(5, "¿Puedo usar la app en varios dispositivos?", "Sí, siempre que uses el mismo usuario y contraseña."),
    FAQItem(6, "¿Qué hago si la app se bloquea?", "Cierra y vuelve a abrir la app. Si persiste, contacta con soporte."),
    FAQItem(7, "¿Dónde cambio mi contraseña o datos personales?", "En 'Perfil' -> 'Configuración de cuenta'."),
    FAQItem(8, "¿Cómo elimino mi cuenta?", "Desde 'Perfil' -> 'Eliminar cuenta', siguiendo las instrucciones.")
)
@Composable
fun PreguntasScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onDeleteAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- CABECERA (Header) ---
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

        // --- CONTENIDO DE PREGUNTAS ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚙️", fontSize = 18.sp) // Emoji de engranaje
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generales",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
            }

            items(listaPreguntas) { item ->
                FAQItemRow(item)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun FAQItemRow(item: FAQItem) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${item.id}. ${item.pregunta}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.respuesta,
            fontSize = 15.sp,
            color = Color(0xFF444444), // Gris oscuro para la respuesta
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 16.dp) // Sangría para la respuesta
        )
    }
}
