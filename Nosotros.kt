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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.CoroutineScope



@Composable
fun NosotrosScreen(
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

        // --- CUERPO DE TEXTO ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Por si el texto es largo en pantallas pequeñas
                .padding(24.dp)
        ) {
            // Párrafo principal con negrita al inicio
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Somos un equipo que cree que el tiempo vale oro.")
                    }
                    append(" Por eso creamos esta aplicación: una herramienta sencilla, moderna y precisa para controlar las horas de trabajo sin complicaciones, sin papeleo y sin perder tiempo.")
                },
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Nuestra misión es ayudar a las empresas a cumplir con la normativa de control horario y, al mismo tiempo, ofrecer a los trabajadores una forma justa y transparente de registrar su jornada.",
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Creemos en la eficiencia, la claridad y la confianza mutua.",
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección: Qué nos diferencia
            Text(
                text = "Qué nos diferencia:",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de puntos (Bullets)
            val puntosDiferenciadores = listOf(
                "Diseño intuitivo: fácil de usar para cualquier perfil.",
                "Datos en tiempo real: cada fichaje se sincroniza al instante.",
                "Privacidad garantizada: cumplimos con el RGPD y usamos cifrado de datos.",
                "Flexibilidad total: adaptable a distintos tipos de jornada, turnos o ubicaciones.",
                "Soporte humano: estamos aquí para ayudarte siempre que lo necesites."
            )

            puntosDiferenciadores.forEach { punto ->
                BulletItem(text = punto)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección: Nuestra visión
            Text(
                text = "Nuestra visión",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Queremos digitalizar la gestión del tiempo laboral con herramientas que simplifiquen la vida diaria en el trabajo. Nuestro objetivo es que fichar sea tan rápido como tomarse un café.",
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun BulletItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "•", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
    }
}