package com.example.fichando

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


data class Documento(
    val id: Int,
    val titulo: String,
    val nombreArchivo: String,
    val recursoRaw: Int, // Cambiado de 'tamaño' a 'recursoRaw'
    val categoria: String, // Añadido porque lo usas en doc.categoria
    var isRead: Boolean = false // Cambiado de String a Boolean
)

@Composable
fun DocumentosScreen(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onDeleteAccount: () -> Unit
) {
    val nominas = remember {
        listOf(
            Documento(1, "Octubre 2025", "Nomina_Octubre_2025.pdf", R.raw.nomina_octubre_2025, "Nominas"),
            Documento(2, "Noviembre 2025", "Nomina_Noviembre_2025.pdf", R.raw.nomina_noviembre_2025, "Nominas"),
            Documento(3, "Diciembre 2025", "Nomina_Diciembre_2025.pdf", R.raw.nomina_diciembre_2025, "Nominas"),
            Documento(4, "Contrato", "Contrato_Trabajo_Maria.pdf", R.raw.contrato, "Documentos")
        )
    }
    val contexto = LocalContext.current // Obtenemos el contexto para poder descargar
    var documentoSeleccionado by remember { mutableStateOf<Documento?>(null) }
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
        Spacer(modifier = Modifier.height(20.dp))
// --- TARJETA DE ARCHIVOS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFFC5D9E5))
                .padding(30.dp)
        ) {
            Text("Nominas", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            nominas.forEach { doc ->
                Row(
                    modifier = Modifier
                        .padding(start = 35.dp, top = 8.dp)
                        .clickable { documentoSeleccionado = doc }, // Seleccionamos el OBJETO
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(id = R.drawable.documento), null, Modifier.size(26.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(doc.titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        // --- ZONA DE DESCARGA DINÁMICA ---
        documentoSeleccionado?.let { doc ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .border(1.dp, Color.LightGray)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFC5D9E5)).padding(10.dp)) {
                    Text(text = doc.categoria, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("• ${doc.titulo}")

                    IconButton(
                        onClick = {
                            // USAMOS EL RECURSO ESPECÍFICO DEL DOCUMENTO
                            val exito = descargarDesdeResources(
                                context = contexto,
                                resourceId = doc.recursoRaw,
                                nombreFinal = doc.nombreArchivo
                            )
                            if (exito) Toast.makeText(contexto, "Descargado: ${doc.nombreArchivo}", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(40.dp).background(Color(0xFF25B7D3), CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.descarga),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

fun descargarDesdeResources(context: Context, resourceId: Int, nombreFinal: String): Boolean {
    return try {
        val inputStream: InputStream = context.resources.openRawResource(resourceId)

        // Localización: Carpeta Downloads pública
        val rutaDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val archivoDestino = File(rutaDescargas, nombreFinal)

        val outputStream = FileOutputStream(archivoDestino)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}