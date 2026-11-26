package com.example.pgl_restosync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pgl_restosync.Model.Local.AppDatabase
import com.example.pgl_restosync.Model.RestauranteRepository
import com.example.pgl_restosync.View.HomeScreen
import com.example.pgl_restosync.View.PantallaInicio
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante
import com.example.pgl_restosync.ui.theme.PGL_RestoSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PGL_RestoSyncTheme {
                val viewModel: ViewModelRestaurante = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            val db = AppDatabase.getDatabase(applicationContext)
                            val repository = RestauranteRepository(
                                db.mesaDao(), db.categoriaDao(), db.platoDao(), db.comandaDao()
                            )
                            ViewModelRestaurante(repository)
                        }
                    }
                )
                HomeScreen(
                    viewModel = viewModel,
                    onMesaAbierta = { mesaId -> /*ALL*/ }
                )
            }
        }
    }
}

