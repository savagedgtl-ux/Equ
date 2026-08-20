package com.equ.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.equ.app.ui.components.ComingSoonBanner

@Composable
fun BookingsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Bookings") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ComingSoonBanner(
                title = "Connect Calendly / cal.com — Coming soon",
                detail = "Once you connect your Calendly or cal.com account, client bookings made through " +
                    "your landing page will show up here. Needs an API key from your Calendly/cal.com developer account.",
            )
        }
    }
}
