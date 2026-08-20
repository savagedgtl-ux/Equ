package com.equ.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.equ.app.data.local.LandingPageConfig
import com.equ.app.ui.components.ComingSoonBanner

@Composable
fun LandingPageScreen(viewModel: LandingPageViewModel) {
    val saved by viewModel.config.collectAsState()

    var displayName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var services by remember { mutableStateOf("") }

    LaunchedEffect(saved) {
        displayName = saved.displayName
        bio = saved.bio
        services = saved.services
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Landing Page") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Draft the page your clients will see. Editing and saving works offline; publishing it to a shareable link needs the hosting backend.",
                style = MaterialTheme.typography.bodyMedium,
            )

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display name") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Short bio") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = services,
                onValueChange = { services = it },
                label = { Text("Services (one per line)") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = { viewModel.save(LandingPageConfig(displayName, bio, services)) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save draft")
            }

            ComingSoonBanner(
                title = "Publish to a shareable link — Coming soon",
                detail = "Publishing hosts this page at a link/QR code clients can open (e.g. equ.app/t/your-name). " +
                    "This needs the hosting backend from the project's ARCHITECTURE.md and isn't wired up yet.",
            )
        }
    }
}
