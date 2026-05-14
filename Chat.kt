package com.example.fichando

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    currentImageUri: Uri?,
    onImageChanged: (Uri) -> Unit,
    onDeleteAccount: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
){
    val scrollState = rememberScrollState()
    var mensaje by remember { mutableStateOf("") }
    var mensajes by remember { mutableStateOf(listOf("AJ: Llegaré 5 minutos tarde", "MH: Mañana necesito ir al médico")) }
    val listState = rememberLazyListState()
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
        // Scroll automático al último mensaje
        LaunchedEffect(mensajes.size) {
            listState.animateScrollToItem(mensajes.size - 1)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(580.dp),
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
                                .widthIn(min = 100.dp, max = 280.dp) // Crece con el texto hasta 280.dp
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Entrada de mensaje
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 1.dp),
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

@Preview(showSystemUi = true)
@Composable
fun ChatPreview(){
    val navController = rememberNavController()
    // 1. Creamos un estado de drawer falso (cerrado por defecto)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // 2. Creamos un scope de corrutinas de prueba
    val scope = rememberCoroutineScope()

    // 3. Llamamos a la función Chat con datos de prueba
    Chat(
        currentImageUri = null, // En el preview no solemos cargar URIs reales
        onImageChanged = { /* No hace nada en el preview */ },
        onDeleteAccount = { /* No hace nada en el preview */ },
        drawerState = drawerState,
        scope = scope
    )
}