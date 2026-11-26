package com.example.pgl_restosync.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.pgl_restosync.Model.Local.PlatoEntity
import com.example.pgl_restosync.R
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoScreen(
    viewModel: ViewModelRestaurante,
    mesaId: Int,
    onVolver: () -> Unit
) {
    val mesas by viewModel.mesas.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val platos by viewModel.platos.collectAsState()

    // Se busca la mesa en la lista para poner el nombre en la barra superior
    val mesa = mesas.find { it.id == mesaId }

    val colorBase = colorResource(R.color.restosycn_primary)
    val snackBarHostState =
        remember { SnackbarHostState() } // honestamente, esto me lo puso el IDE y funciona, asi que confio
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        mesa?.let { "${it.numero} - ${it.comensales} comensales" } ?: "Pedido"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorBase,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Menú lateral izquierdo con opcion de cada catedoria
            NavigationRail(
                containerColor = colorBase,
                windowInsets = WindowInsets(0,0,0,0)
            ) {
                categorias.forEach { categoria ->
                    NavigationRailItem(
                        selected = categoria.id == categoriaSeleccionada,
                        onClick = { viewModel.seleccionarCategoria(categoria.id) },
                        icon = {
                            Icon(
                                painter = painterResource(id = iconoCategoria(categoria.id)),
                                contentDescription = categoria.nombre,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        label = { Text(categoria.nombre) },
                        colors = NavigationRailItemDefaults.colors(
                            indicatorColor = Color.White,
                            selectedIconColor = colorBase,
                            selectedTextColor = Color.White,
                            // Pestañas no seleccionadas: blanco un poco apagado
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            // Y aqui iría la lista de platos con otro LazyVerticalGrid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(platos, key = { it.id }) { plato ->
                    TarjetaPlato(
                        plato = plato,
                        onClick = {
                            viewModel.agregarPlatoMesa(mesaId, plato.id, 1)
                            scope.launch {
                                snackBarHostState.currentSnackbarData?.dismiss()
                                snackBarHostState.showSnackbar("${plato.nombre} agregado")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaPlato(
    plato: PlatoEntity,
    onClick: () -> Unit
) {
    val colorBase = colorResource(R.color.restosycn_primary)
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.restosycn_primary))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // La idea es que la imagen ocupe el espacio que deja el texto
            Image(
                painter = painterResource(id = imagenPlato(plato.id)),
                contentDescription = plato.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            // Textos con nombre y precio
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = plato.nombre,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "%.2f €".format(plato.precio),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Esta función traduce el id de la categoria a su icono correspondiente
private fun iconoCategoria(categoriaId: Int): Int = when (categoriaId) {
    1 -> R.drawable.entrantes
    2 -> R.drawable.ensaladas
    3 -> R.drawable.pizzas
    4 -> R.drawable.pastas
    5 -> R.drawable.postres
    6 -> R.drawable.bebidas
    else -> R.drawable.entrantes
}

// Lo mismo, pero con las imágenes de los platos
private fun imagenPlato(platoId: Int): Int = when (platoId) {
    1 -> R.drawable.hummus_de_berenjena
    2 -> R.drawable.foccacia_pomodorini_e_burrata
    3 -> R.drawable.cesar_romana
    4 -> R.drawable.ventresca_e_mandorle
    5 -> R.drawable.capriccio_de_higo_y_cabra
    6 -> R.drawable.pergamino_di_porchetta
    7 -> R.drawable.gnocchi_molto_molto_gorgonzola_con_jam_n_iberico
    8 -> R.drawable.spaghetti_carbonara_trufada
    9 -> R.drawable.tiramisu
    10 -> R.drawable.panna_coco_tta
    11 -> R.drawable.estrella_galicia_un_tercio
    12 -> R.drawable.estrella_0_0
    else -> R.drawable.logo_del_proyecto__app_  // si no tiene foto, le pongo el icono de la app
}