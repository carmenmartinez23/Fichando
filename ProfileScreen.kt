package com.example.fichando

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Necesitarás añadir la librería de Coil en build.gradle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentImageUri: Uri?,
    onImageChanged: (Uri) -> Unit,
    onDeleteAccount: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Estados para alertas
    var showPasswordAlert by remember { mutableStateOf(false) }
    var showDeleteAlert by remember { mutableStateOf(false) }

    // Selector de imágenes
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageChanged(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- SECCIÓN IMAGEN DE PERFIL ---
        Box(contentAlignment = Alignment.BottomEnd) {
            if (currentImageUri != null) {
                AsyncImage(
                    model = currentImageUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF00C6FF), CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Imagen por defecto si no hay URI
                Image(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "Perfil",
                    modifier = Modifier.size(150.dp).clip(CircleShape)
                )
            }
        }

        TextButton(onClick = { launcher.launch("image/*") }) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Subir imagen nueva", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- CONFIGURACIÓN DE CUENTA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Configuración de la cuenta", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                ProfileTextField(label = "Usuario", value = "maria.2016@gmail.com")
                ProfileTextField(label = "Contraseña", value = "************")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Contraseña nueva") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { showPasswordAlert = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD35400)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(55.dp)
                    ) {
                        Text("Cambiar\ncontraseña", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- DATOS PERSONALES (Captura 130133) ---
        Text("Datos personales:", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold)
        ProfileTextField(label = "DNI", value = "587469215J")
        ProfileTextField(label = "Nombre", value = "María")
        ProfileTextField(label = "Apellidos", value = "Hernandez Ochoa")
        ProfileTextField(label = "Domicilio", value = "Calle Folclore, 82")
        ProfileTextField(label = "Población", value = "Valencia")
        ProfileTextField(label = "Provincia", value = "Valencia")
        ProfileTextField(label = "País", value = "España")

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN ELIMINAR ---
        Button(
            onClick = { showDeleteAlert = true },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BB5)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Eliminar cuenta", fontWeight = FontWeight.Bold)
        }
    }

    // --- DIÁLOGOS ---
    if (showPasswordAlert) {
        AlertDialog(
            onDismissRequest = { showPasswordAlert = false },
            title = { Text("Éxito") },
            text = { Text("La contraseña se ha cambiado correctamente.") },
            confirmButton = {
                TextButton(onClick = { showPasswordAlert = false }) { Text("Cerrar") }
            }
        )
    }

    if (showDeleteAlert) {
        AlertDialog(
            onDismissRequest = { showDeleteAlert = false },
            title = { Text("Borrar cuenta") },
            text = { Text("¿De verdad quiere borrar la cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteAlert = false
                    onDeleteAccount()
                }) { Text("Sí, borrar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAlert = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ProfileTextField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        readOnly = true // Para que parezca un formulario de datos ya rellenos
    )
}