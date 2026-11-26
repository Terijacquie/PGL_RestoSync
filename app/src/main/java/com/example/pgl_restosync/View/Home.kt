package com.example.pgl_restosync.View


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgl_restosync.Model.Local.FormaMesa
import com.example.pgl_restosync.Model.Local.MesaEntity
import com.example.pgl_restosync.R
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante
import com.example.pgl_restosync.ui.theme.PGL_RestoSyncTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ViewModelRestaurante,
    onMesaAbierta: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listaMesas by viewModel.mesas.collectAsState()
    // Guarda la mesa que se ha pulsado para abri. Si es null, no se muestra el diálogo
    var mesaSeleccionada by remember { mutableStateOf<MesaEntity?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_pantallainicio2),
            contentDescription = "Fondo de pantalla inicio",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .blur(12.dp)
                .fillMaxSize(),
            colorFilter = ColorFilter.tint(
                color = Color.Black.copy(alpha = 0.4f), // Porcentaje de opacidad
                blendMode = BlendMode.SrcOver // Aplica el negro como una capa encima
            )
        )
    }
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
    ){ innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )  {
            items(listaMesas, key = {it.id}) {mesa ->
                TarjetaMesa(
                    mesa = mesa,
                    onClick = {
                        if (mesa.isOpen) onMesaAbierta(mesa.id)
                        else mesaSeleccionada = mesa
                    }
                )
            }
        }
    }

    // Si hay una mesa seleccionada, se pinta el dialogo encima
    mesaSeleccionada?.let { mesa ->
        DialogoComensales(
            mesa = mesa,
            onConfirmar = { comensales ->
                viewModel.abrirMesa(mesa.id, comensales)
                mesaSeleccionada = null
            },
            onCancelar = {
                mesaSeleccionada = null
            }
        )
    }
}

@Composable
fun TarjetaMesa(
    mesa: MesaEntity,
    onClick: () -> Unit
) {
    val colorBase = colorResource(R.color.restosycn_primary)
    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            // si esta abierta, semitransparente, si esta cerrado, color solido
            containerColor = if (mesa.isOpen) colorBase.copy(alpha = 0.5f) else colorBase)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconoMesa(mesa.forma)),
                contentDescription = "Icono mesa",
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = mesa.numero,
                color = Color.White,
            )
            if (mesa.isOpen) {
                Text(
                    text = "${mesa.comensales} comensales", color = Color.White
                )
            }
        }
    }
}

@Composable
fun DialogoComensales(
    mesa: MesaEntity,
    onConfirmar: (Int) -> Unit,
    onCancelar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    val numero = texto.toIntOrNull()

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Abrir ${mesa.numero}") },
        text = {
            OutlinedTextField(
                value = texto,
                // solo deja escribir dígitos, máximo 2
                onValueChange = { nuevo ->
                    if (nuevo.all { it.isDigit() } && nuevo.length <= 2) texto = nuevo
                },
                label = { Text("Número de comensales")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(
                onClick = { numero?.let(onConfirmar) },
                enabled = numero != null && numero > 0
            ) { Text("Abrir mesa") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}


// Ahora, con esta función, asigno el icono de cada campo de la clase enum FormaMesa, automatizando así la creación
// en caso de que en un futuro se añadan más mesas en la base de datos, ya que, en un futuro, planeo agregar la función para que el restaurante añada las mesas que les parezca conveniente
private fun iconoMesa(forma: FormaMesa): Int = when (forma) {
    FormaMesa.CUADRADA -> R.drawable.mesa_cuadrada_icono
    FormaMesa.REDONDA_CUATRO_ASIENTOS -> R.drawable.mesa_redonda_cuatro_asientos
    FormaMesa.REDONDA_CINCO_ASIENTOS -> R.drawable.mesa_redonda_icono
    FormaMesa.RECTANGULAR -> R.drawable.mesa_rectangular
}

/*@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PGL_RestoSyncTheme {
        HomeScreen()
    }
}*/