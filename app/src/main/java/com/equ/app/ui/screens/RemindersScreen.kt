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
fun RemindersScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Reminders & Recaps") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ComingSoonBanner(
                title = "Session reminders — Coming soon",
                detail = "Push reminders before upcoming sessions need Firebase Cloud Messaging plus the " +
                    "booking webhook from Calendly/cal.com to know when sessions are scheduled.",
            )
            ComingSoonBanner(
                title = "Session recaps — Coming soon",
                detail = "A recap view of past sessions needs the same booking webhook data to know which " +
                    "sessions happened.",
            )
        }
    }
}
