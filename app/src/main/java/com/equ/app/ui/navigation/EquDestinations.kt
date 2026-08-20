package com.equ.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Web
import androidx.compose.ui.graphics.vector.ImageVector

sealed class EquDestination(val route: String, val label: String, val icon: ImageVector) {
    data object LandingPage : EquDestination("landing_page", "Landing Page", Icons.Filled.Web)
    data object Bookings : EquDestination("bookings", "Bookings", Icons.Filled.CalendarMonth)
    data object Reminders : EquDestination("reminders", "Reminders", Icons.Filled.Notifications)
    data object Clients : EquDestination("clients", "Clients", Icons.Filled.People)
}

const val CLIENT_DETAIL_ROUTE = "client_detail"
const val CLIENT_ID_ARG = "clientId"

val bottomNavDestinations = listOf(
    EquDestination.LandingPage,
    EquDestination.Bookings,
    EquDestination.Reminders,
    EquDestination.Clients,
)
