package com.equ.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.equ.app.data.local.ClientEntity

@Composable
fun ClientsScreen(
    viewModel: ClientsViewModel,
    onAddClient: () -> Unit,
    onOpenClient: (Long) -> Unit,
) {
    val clients by viewModel.clients.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClient) {
                Icon(Icons.Filled.Add, contentDescription = "Add client")
            }
        },
    ) { padding ->
        if (clients.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    text = "No clients yet.\nTap + to add your first client profile.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(clients, key = { it.id }) { client ->
                    ClientRow(client = client, onClick = { onOpenClient(client.id) })
                }
            }
        }
    }
}

@Composable
private fun ClientRow(client: ClientEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = client.name, style = MaterialTheme.typography.titleMedium)
            if (client.contactInfo.isNotBlank()) {
                Text(text = client.contactInfo, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
