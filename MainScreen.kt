package com.example.fichando

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Barra Superior (Header)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Maria", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Operador general", fontSize = 12.sp, color = Color.Gray)
            }

            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(40.dp).border(2.dp, Color(0xFF00C6FF), CircleShape),
                tint = Color(0xFF00C6FF)
            )
        }

        Divider(thickness = 1.dp, color = Color.LightGray)

        // 2. Botón Central de Fichaje (Cronómetro)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(200.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                // Aquí iría el icono del cronómetro (puedes usar un Image)
                Box(contentAlignment = Alignment.Center) {
                    Text("⏱️", fontSize = 80.sp) // Representación temporal
                }
            }
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
                Spacer(modifier = Modifier.height(100.dp).fillMaxWidth().background(Color.White.copy(alpha = 0.5f)))
                Text("Simulación Calendario", modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.LightGray)
            }
        }

        // 4. Sección Chat
        Text(
            text = "Chat",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBEE3F8))
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ChatBubble("AJ", "Llegaré 5 minutos tarde")
                ChatBubble("MH", "Mañana necesito ir al médico")
            }
        }

        // Entrada de mensaje
        Text(
            text = "Mandar mensaje al grupo:",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 12.sp, color = Color.Gray
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(15.dp)
            )
            IconButton(onClick = {}) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color(0xFF4299E1))
            }
        }
    }
}

@Composable
fun ChatBubble(user: String, message: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(20.dp).border(1.dp, Color.Gray, CircleShape)) {
            Text(user, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
        }
        Surface(
            modifier = Modifier.padding(start = 4.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF0091FF)
        ) {
            Text(message, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
