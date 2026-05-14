package com.example.fichando

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Degradado fiel: De Azul Cyan suave a Naranja Coral suave (diagonal)
            val gradientBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF00C6FF), Color(0xFFFF7E5F)),
                start = Offset(0f, 0f),
                end = Offset(width, height * 0.4f)
            )

            drawRect(
                brush = gradientBrush,
                size = Size(width, height * 0.4f),
                topLeft = Offset(0f, 0f)
            )

            // 2. Curva blanca: Ajustada para que sea más suave y empiece más abajo
            val curvePath = Path().apply {
                moveTo(0f, height * 0.20f) // Empezamos a dibujar un poco más abajo
                cubicTo(
                    width * 0.2f, height * 0.30f, // Primer control
                    width * 0.9f, height * 0.20f,  // Segundo control
                    width, height * 0.2f        // Final de la curva
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(path = curvePath, color = Color.White)
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Distancia al título "LOGIN"
            Spacer(modifier = Modifier.height(80.dp))

            // Título fiel: Blanco, Negrita, Grande
            Text(
                text = "LOGIN",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            // Espacio más grande entre LOGIN y Email (como en la foto)
            Spacer(modifier = Modifier.height(110.dp))

            // Etiquetas fieles: Negrita e Itálica
            Text(
                text = "Email",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("example@email.com") },
                // Bordes fieles: Rectangulares con esquinas un poco suaves
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Etiquetas fieles: Negrita e Itálica
            Text(
                text = "Contraseña",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                // Bordes fieles: Rectangulares con esquinas un poco suaves
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón fiel: Azul oscuro, Rectangular suave, texto en Mayúsculas
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006699)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Entrar".uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Textos de ayuda azul claro (como en tu foto)
            Text(text = "¿Has olvidado tu usuario?", color = Color(0xFF0088CC), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "¿Has olvidado tu contraseña?", color = Color(0xFF0088CC), fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    // Le pasamos unas llaves vacías { } porque en la preview no necesitamos navegar
    LoginScreen(onLoginSuccess = {})
}