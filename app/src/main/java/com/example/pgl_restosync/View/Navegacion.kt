package com.example.pgl_restosync.View

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pgl_restosync.R
import com.example.pgl_restosync.RestoSyncApp
import com.example.pgl_restosync.ViewModel.DetalleComandaViewModel
import com.example.pgl_restosync.ViewModel.DetalleComandaViewModelFactory
import com.example.pgl_restosync.ViewModel.ViewModelRestaurante

sealed class Navegacion(val path: String, val label: String, val icon: ImageVector) {

    object Login : Navegacion(
        path = "login",
        label = "Inicio",
        icon = Icons.Filled.Person
    )

    object Mesas : Navegacion(
        path = "home",
        label = "Mesas",
        icon = Icons.Filled.TableRestaurant
    )

    object Comandas : Navegacion(
        path = "comandas",
        label = "Comandas",
        icon = Icons.Filled.Receipt
    )

    //Aqui va la ruta dinamica para el detalle
    object Pedido : Navegacion(
        path = "pedido/{mesaId}",
        label = "Pedido",
        icon = Icons.Filled.RestaurantMenu
    ) {
        fun crearRuta(mesaId: Int) = "pedido/$mesaId"
    }

    object Detalle : Navegacion(
        path = "detalle/{mesaId}",
        label = "Detalle",
        icon = Icons.Filled.Receipt
    ) {
        fun crearRuta(mesaId: Int) = "detalle/$mesaId"
    }
}

@Composable
fun AppRestoSyncNavegacion(viewModel: ViewModelRestaurante) {
    val navController = rememberNavController()

    // Ruta en la que se está actualmente
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    //Pantallas que salen en el bottombar
    val opcionesMenu = listOf(Navegacion.Mesas, Navegacion.Comandas)
    // Esto es necesario, porque la barra no quiero que aparezca en el login, detalle ni en el pedido
    val mostrarBarra = opcionesMenu.any { it.path == rutaActual }

    val colorBase = colorResource(R.color.restosycn_primary)

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = colorBase) {
                    opcionesMenu.forEach { opcion ->
                        NavigationBarItem(
                            selected = rutaActual == opcion.path,
                            onClick = {
                                if (rutaActual != opcion.path) {
                                    navController.navigate(opcion.path) {
                                        // Evita que se apilen pantallas repetidas
                                        popUpTo(Navegacion.Mesas.path) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(opcion.icon, contentDescription = opcion.label) },
                            label = { Text(opcion.label) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White,
                                selectedIconColor = colorBase,
                                selectedTextColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navegacion.Login.path,
            modifier = Modifier.padding(
                bottom = if (mostrarBarra) innerPadding.calculateBottomPadding() else 0.dp
            )
        ) {
            //1. login (por ahora, entra sin usuario ni contraseña)
            composable(Navegacion.Login.path) {
                PantallaInicio(
                    onLogin = {
                        navController.navigate(Navegacion.Mesas.path) {
                            // si presionamos el back en las mesas, cierra la app
                            popUpTo(Navegacion.Login.path) { inclusive = true }
                        }
                    }
                )
            }

            // 2. mesas
            composable(Navegacion.Mesas.path) {
                HomeScreen(
                    viewModel = viewModel,
                    onMesaAbierta = { mesaId ->
                        navController.navigate(Navegacion.Pedido.crearRuta(mesaId))
                    },
                    onCerrarSesion = {
                        navController.navigate(Navegacion.Login.path) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 3. Lista de comandas
            composable(Navegacion.Comandas.path) {
                ResumenPedidos(
                    viewModel = viewModel,
                    onComandaClick = { mesaId ->
                        navController.navigate(Navegacion.Detalle.crearRuta(mesaId))
                    },
                    modifier = Modifier
                )
            }

            // 4. Elecion de platos. Lee el mesaId de la ruta
            composable(
                route = Navegacion.Pedido.path,
                arguments = listOf(
                    navArgument("mesaId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
                PedidoScreen(
                    viewModel = viewModel,
                    mesaId = mesaId,
                    onVolver = { navController.popBackStack() }
                )
            }

            // 5. Detalle de la comanda. lee mesaId e inyecta su propio viewmodel
            composable(
                route = Navegacion.Detalle.path,
                arguments = listOf(
                    navArgument("mesaId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val mesaId = backStackEntry.arguments?.getInt("mesaId") ?: 0
                val context = LocalContext.current
                val app = context.applicationContext as RestoSyncApp

                val detalleViewModel: DetalleComandaViewModel = viewModel(
                    factory = DetalleComandaViewModelFactory(app.repository)
                )

                DetalleComandaScreen(
                    mesaId = mesaId,
                    viewModel = detalleViewModel,
                    onVolver = { navController.popBackStack() },
                    onAnadirPlato = { id ->
                        navController.navigate(Navegacion.Pedido.crearRuta(id))
                    }
                )

            }
        }
    }
}








