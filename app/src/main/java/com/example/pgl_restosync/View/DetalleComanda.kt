package com.example.pgl_restosync.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pgl_restosync.Model.Local.AuxiliarDetalleComanda
import com.example.pgl_restosync.R
import com.example.pgl_restosync.ViewModel.DetalleComandaViewModel
import com.example.pgl_restosync.ViewModel.DetalleUiState
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleComandaScreen(
    mesaId: Int,
    viewModel: DetalleComandaViewModel,
    onVolver: () -> Unit,
    onAnadirPlato: (Int) -> Unit
) {
    // Igual que en DetalleLibro: al entrar, se pide al ViewModel que cargue la mesa
    LaunchedEffect(mesaId) {
        viewModel.cargarDetalleComanda(mesaId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colorBase = colorResource(R.color.restosycn_primary)

    // El título solo se conoce cuando ya ha cargado
    val titulo = (uiState as? DetalleUiState.Exito)?.numeroMesa ?: stringResource(R.string.detalle_titulo_defecto)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                // El botón para añadir un plato
                actions = {
                    IconButton(onClick = { onAnadirPlato(mesaId) }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Plato")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorBase,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        // Barra baja con el total: solo sale cuando hay datos
        bottomBar = {
            val estado = uiState
            if (estado is DetalleUiState.Exito) {
                BottomAppBar(containerColor = colorBase) {
                    Text(
                        text = "Total",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    Text(
                        text = "%.2f €".format(estado.total),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        val modificadorContenido = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (val state = uiState) {
            // Mientras carga: circulito
            is DetalleUiState.Cargando -> {
                Box(modifier = modificadorContenido, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorBase)
                }
            }
            // Si algo falla: mensaje de error
            is DetalleUiState.Error -> {
                Box(modifier = modificadorContenido, contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error: ${state.mensaje}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            // Datos listos
            is DetalleUiState.Exito -> {
                if (state.lineas.isEmpty()) {
                    // Mensajito si no hay nada en la comanda
                    Box(modifier = modificadorContenido, contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.detalle_vacio),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = modificadorContenido,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.lineas, key = { it.id }) { linea ->
                            LineaComanda(
                                linea = linea,
                                onEliminar = { viewModel.eliminarDetalleComanda(linea.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Con esto planeo que en una linea apareza un "x2" de xPlato y el total
@Composable
fun LineaComanda(
    linea: AuxiliarDetalleComanda,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${linea.cantidad}x",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = linea.platoNombre,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "%.2f €/ud.".format(linea.precio),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "%.2f".format(linea.precio * linea.cantidad),
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}


// Nota: Como "lineas" se actualiza solo, el precio cambia si eliminas o añades un plato


























