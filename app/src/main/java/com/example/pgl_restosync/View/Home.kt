package com.example.pgl_restosync.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgl_restosync.R
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante
import com.example.pgl_restosync.ui.theme.PGL_RestoSyncTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ViewModelRestaurante,
    modifier: Modifier = Modifier
) {
    val listaMesas = viewModel.mesas.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_pantallainicio2),
            contentDescription = "Fondo de pantalla inicio",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .blur(12.dp)
                .fillMaxSize(),
            // Quise poner un poco más oscuro el fondo porque se
            // confundía fácilmente con los colores del tema
            colorFilter = ColorFilter.tint(
                color = Color.Black.copy(alpha = 0.4f), // Porcentaje de opacidad
                blendMode = BlendMode.SrcOver // Aplica el negro como una capa encima
            )
        )

        Scaffold(
            containerColor = Color.Transparent, // Esto es para que se vea la imagen de atrás
            topBar = {
                TopAppBar(
                    title = { Text("¡Hola! ¿Qué mesa vamos a servir?") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.restosycn_primary)
                            .copy(alpha = 0.5f),
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    expandedHeight = 100.dp
                )

            },

        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 1.dp, vertical = 40.dp)
                    //.background(colorResource(R.color.restosycn_primary))
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp), // voy a salir, recordar que estoy tanteando los espacios horizontales
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.restosycn_primary)
                                    .copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.weight(1f).padding(4.dp),
                                    contentDescription = "Mesa 1",
                                    painter = painterResource(id = R.drawable.mesa_cuadrada_icono),
                                    tint = Color.White
                                )
                                Text(
                                    text = "Mesa 1",
                                    color = Color.White
                                )
                            }
                        }
                        Card(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.restosycn_primary)
                                    .copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.weight(1f).padding(2.dp),
                                    contentDescription = "Mesa 2",
                                    painter = painterResource(id = R.drawable.mesa_redonda_cuatro_asientos),
                                    tint = Color.White
                                )
                                Text(
                                    text = "Mesa 2",
                                    color = Color.White
                                )
                            }
                        }
                    }
                    // Fila 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.restosycn_primary)
                                    .copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.weight(1f).padding(4.dp),
                                    contentDescription = "Mesa 3",
                                    painter = painterResource(id = R.drawable.mesa_redonda_icono),
                                    tint = Color.White
                                )
                                Text(
                                    text = "Mesa 3",
                                    color = Color.White
                                )
                            }
                        }
                        Card(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.restosycn_primary)
                                    .copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.weight(1f).padding(2.dp),
                                    contentDescription = "Mesa 4",
                                    painter = painterResource(id = R.drawable.mesa_rectangular),
                                    tint = Color.White
                                )
                                Text(
                                    text = "Mesa 4",
                                    color = Color.White
                                )
                            }
                        }
                    }
                    // Fila 3
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp), // voy a salir, recordar que estoy tanteando los espacios horizontales
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Card(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.restosycn_primary)
                                    .copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.weight(1f).padding(4.dp),
                                    contentDescription = "Mesa 5",
                                    painter = painterResource(id = R.drawable.mesa_cuadrada_icono),
                                    tint = Color.White
                                )
                                Text(
                                    text = "Mesa 5",
                                    color = Color.White
                                )
                            }
                        }
                        Card(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.size(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.restosycn_primary)
                                    .copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.weight(1f).padding(2.dp),
                                    contentDescription = "Mesa 6",
                                    painter = painterResource(id = R.drawable.mesa_redonda_cuatro_asientos),
                                    tint = Color.White
                                )
                                Text(
                                    text = "Mesa 6",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PGL_RestoSyncTheme {
        HomeScreen()
    }
}*/