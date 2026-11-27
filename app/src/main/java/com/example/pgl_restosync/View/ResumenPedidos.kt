package com.example.pgl_restosync.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante

@Composable
fun ResumenPedidos(
    viewModel: ViewModelRestaurante,
    onComandaClick: (Int) -> Unit,
    modifier: Modifier
) {
    val comandas by viewModel.listaComandas.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = comandas, key = { c -> c.id}) { comanda ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onComandaClick(comanda.id)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mesa ${comanda(obtenerNumeroMesa(mesaId))}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                }

            }
        }
    }
}