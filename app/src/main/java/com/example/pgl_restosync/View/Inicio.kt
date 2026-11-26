package com.example.pgl_restosync.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Markunread
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgl_restosync.R
import com.example.pgl_restosync.ui.theme.PGL_RestoSyncTheme

@Composable
fun PantallaInicio(modifier: Modifier = Modifier) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
    }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_del_proyecto__app_),
            contentDescription = "Logo",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.padding(bottom = 64.dp, top = 40.dp)
                .size(200.dp)
                .background(colorResource(
                    R.color.restosycn_primary).
                    copy(alpha = 0.5f),
                    shape = CircleShape
                )
        )
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Email") },
            singleLine = true,
            placeholder = { Text("Usuario") },
            modifier = Modifier.padding(bottom = 16.dp),
            // Solo pruebas
            colors = OutlinedTextFieldDefaults.colors(
                // Color de fondo del TextField (enfocado o desenfocado)
                focusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                // Colores del borde para que sea armonico
                focusedBorderColor = colorResource(R.color.restosycn_primary),
                unfocusedBorderColor = colorResource(R.color.restosycn_primary),
                // Colores del texto y la etiqueta
                focusedLabelColor = colorResource(R.color.restosycn_primary),
                unfocusedLabelColor = Color.Black,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Markunread,
                    contentDescription = "User",
                    tint = colorResource(R.color.restosycn_primary)
                )
            },

        )
        //Spacer(modifier = Modifier.width(200.dp))

        OutlinedTextField(
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Contraseña") },
            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                // Color de fondo del TextField (enfocado o desenfocado)
                focusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                // Colores del borde para que sea armónico
                focusedBorderColor = colorResource(R.color.restosycn_primary),
                unfocusedBorderColor = colorResource(R.color.restosycn_primary),
                // Colores del texto y la etiqueta
                focusedLabelColor = colorResource(R.color.restosycn_primary),
                unfocusedLabelColor = Color.Black,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Key,
                    contentDescription = "password",
                    tint = colorResource(R.color.restosycn_primary)
                )
            },
        )

        ElevatedButton(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.elevatedButtonColors(
                //color del contenedor
                containerColor = colorResource(R.color.restosycn_primary),
                //color del texto
                contentColor = Color.White
            ),
            modifier = Modifier.padding(top = 30.dp)
                .size(width = 200.dp, height = 60.dp),
        ) {
            Text("Iniciar Sesión")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InicioPreview() {
    PGL_RestoSyncTheme {
        PantallaInicio()
    }
}