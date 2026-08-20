package com.equ.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.equ.app.data.local.AppDatabase
import com.equ.app.data.local.LandingPagePrefs
import com.equ.app.data.repository.ClientRepository
import com.equ.app.ui.screens.BookingsScreen
import com.equ.app.ui.screens.ClientDetailScreen
import com.equ.app.ui.screens.ClientsScreen
import com.equ.app.ui.screens.ClientsViewModel
import com.equ.app.ui.screens.LandingPageScreen
import com.equ.app.ui.screens.LandingPageViewModel
import com.equ.app.ui.screens.RemindersScreen

@Composable
fun EquNavHost() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current

    val clientsViewModel: ClientsViewModel = viewModel(
        factory = ClientsViewModel.Factory(ClientRepository(AppDatabase.get(context).clientDao())),
    )
    val landingPageViewModel: LandingPageViewModel = viewModel(
        factory = LandingPageViewModel.Factory(LandingPagePrefs(context)),
    )

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            NavigationBar {
                bottomNavDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { androidx.compose.material3.Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = EquDestination.LandingPage.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(EquDestination.LandingPage.route) {
                LandingPageScreen(viewModel = landingPageViewModel)
            }
            composable(EquDestination.Bookings.route) {
                BookingsScreen()
            }
            composable(EquDestination.Reminders.route) {
                RemindersScreen()
            }
            composable(EquDestination.Clients.route) {
                ClientsScreen(
                    viewModel = clientsViewModel,
                    onAddClient = { navController.navigate("$CLIENT_DETAIL_ROUTE/new") },
                    onOpenClient = { id -> navController.navigate("$CLIENT_DETAIL_ROUTE/$id") },
                )
            }
            composable(
                route = "$CLIENT_DETAIL_ROUTE/{$CLIENT_ID_ARG}",
                arguments = listOf(navArgument(CLIENT_ID_ARG) { type = NavType.StringType }),
            ) { backStackEntry ->
                val raw = backStackEntry.arguments?.getString(CLIENT_ID_ARG)
                val clientId = raw?.toLongOrNull()
                ClientDetailScreen(
                    clientId = clientId,
                    viewModel = clientsViewModel,
                    onDone = { navController.popBackStack() },
                )
            }
        }
    }
}
