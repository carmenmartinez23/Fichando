package com.example.fichando

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.example.fichando.ui.theme.FichandoTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FichandoTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var globalProfileImage by remember { mutableStateOf<Uri?>(null) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Solo mostramos el Drawer si NO estamos en el login
                val showDrawer = currentRoute != "pantalla_login"

                // ESTRUCTURA GLOBAL
                if (currentRoute == "pantalla_login") {
                    NavHost(navController = navController, startDestination = "pantalla_login") {
                        composable("pantalla_login") {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate("pantalla_principal") {
                                    // Limpiamos el historial para que no pueda volver al login con el botón atrás
                                    popUpTo("pantalla_login") { inclusive = true }
                                }
                            })
                        }
                        // Necesitamos declarar las otras rutas aquí también para que el navController las reconozca
                        composable("pantalla_principal") { /* Se manejará en el otro bloque */ }
                        composable("profile") { /* Se manejará en el otro bloque */ }
                    }
                } else {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
//                    gesturesEnabled = showDrawer, // Desactivar gestos en el login
                        drawerContent = {
                            if (showDrawer) {
                                MyDrawerContent(
                                    navController = navController,
                                    drawerState = drawerState,
                                    scope = scope,
                                    profileImage = globalProfileImage,
                                    onLogout = {
                                        navController.navigate("pantalla_login") {
                                            popUpTo(0)
                                        }
                                    }
                                )
                            }
                        }
                    ) {
                        Scaffold { paddingValues ->
                            Box(modifier = Modifier.padding(paddingValues)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = "pantalla_login"
                                ) {
                                    composable("pantalla_login") {
                                        LoginScreen(onLoginSuccess = {
                                            navController.navigate("pantalla_principal")
                                        })
                                    }

                                    composable("pantalla_principal") {
                                        MainScreen(
                                            navController = navController,
                                            drawerState = drawerState,
                                            scope = scope,
                                            globalProfileImage = globalProfileImage,
                                            onLogout = {
                                                navController.navigate("pantalla_login") {
                                                    popUpTo(0)
                                                }
                                            }
                                        )
                                    }

                                    composable("profile") {
                                        ProfileScreen(
                                            currentImageUri = globalProfileImage,
                                            onImageChanged = { globalProfileImage = it },
                                            onDeleteAccount = {
                                                navController.navigate("pantalla_login") {
                                                    popUpTo(0)
                                                }
                                            }
                                        )
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
    fun MyDrawerContent(
        navController: NavHostController,
        drawerState: DrawerState,
        scope: CoroutineScope,
        profileImage: Uri?,
        onLogout: () -> Unit
    ) {
        ModalDrawerSheet(
            drawerContainerColor = Color(0xFF1A6699) // El azul de tu app
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- CABECERA: Nombre y Foto ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Maria",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Operador general",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 20.sp
                    )
                }

                // Foto dinámica
                Box {
                    if (profileImage != null) {
                        AsyncImage(
                            model = profileImage,
                            contentDescription = null,
                            modifier = Modifier.size(70.dp).clip(CircleShape)
                                .border(2.dp, Color(0xFF00C6FF), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.perfil),
                            contentDescription = null,
                            modifier = Modifier.size(70.dp).clip(CircleShape)
                                .border(2.dp, Color(0xFF00C6FF), CircleShape)
                        )
                    }
                }
            }

            Divider(
                color = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // --- CUERPO: Lista de opciones (Usando la lista menuOptions) ---
            menuOptions.forEach { option ->
                NavigationDrawerItem(
                    icon = {
                        Icon(option.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(30.dp , 30.dp)
                        )},
                    label = {
                        Column {
                            Text(
                                option.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            if (option.subtitle.isNotEmpty()) {
                                Text(
                                    option.subtitle,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 15.sp
                                )
                            }
                        }
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() } // Cerramos el menú

                        // Lógica de navegación según el título
                        if (option.title == "Perfil") {
                            navController.navigate("profile")
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- PIE: Botón Desconectar ---
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Desconectar", color = Color(0xFF1A6699), fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FichandoTheme {
        Greeting("Android")
    }
}
