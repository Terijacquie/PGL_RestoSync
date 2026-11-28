package com.example.pgl_restosync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgl_restosync.View.AppRestoSyncNavegacion
import com.example.pgl_restosync.ViewModel.RestauranteViewModelFactory
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante
import com.example.pgl_restosync.ui.theme.PGL_RestoSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // El repositorio ya lo crea RestoSyncApp; aquí solo lo recogemos
        val app = application as RestoSyncApp

        setContent {
            PGL_RestoSyncTheme {
                val viewModel: ViewModelRestaurante =
                    viewModel(factory = RestauranteViewModelFactory(app.repository))
                    AppRestoSyncNavegacion(viewModel = viewModel)
            }
        }
    }
}

