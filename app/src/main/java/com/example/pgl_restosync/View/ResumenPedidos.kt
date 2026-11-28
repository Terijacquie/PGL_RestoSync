package com.example.pgl_restosync.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pgl_restosync.Model.Local.ResumenComanda
import com.example.pgl_restosync.R
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenPedidos(
    viewModel: ViewModelRestaurante,
    onComandaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val comandas by viewModel.resumenComandas.collectAsStateWithLifecycle()
    val colorBase = colorResource(R.color.restosycn_primary)

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.resumen_titulo)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorBase,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = comandas, key = { it.comandaId }) { comanda ->
                TarjetaComanda(
                    comanda = comanda,
                    // Aqui le paso la mesa porque el metodo busca la mesa por id
                    onClick = { if(comanda.isActiva) onComandaClick(comanda.mesaId) },
                    onEliminar = { viewModel.eliminarComanda(comanda) }
                )
            }
        }
    }
}

@Composable
fun TarjetaComanda(
    // Se importa la clase nueva creada para el resumen de la comanda
    comanda: ResumenComanda,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Nombre y estado con el boton de eliminar en caso de equivocación o limpieza de historial
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = comanda.numeroMesa,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (comanda.isActiva) "Comanda activa" else "Comanda cerrada",
                    color = if (comanda.isActiva) Color.Green else Color.Gray
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}