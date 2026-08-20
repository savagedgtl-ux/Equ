package com.equ.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.equ.app.data.local.ClientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

@Composable
fun ClientDetailScreen(
    clientId: Long?,
    viewModel: ClientsViewModel,
    onDone: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    var existing by remember { mutableStateOf<ClientEntity?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(clientId) {
        if (clientId != null) {
            viewModel.getClient(clientId)?.let {
                existing = it
                name = it.name
                contactInfo = it.contactInfo
                notes = it.notes
                photoUri = it.photoUri
            }
        }
    }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Copy into app-private storage: a picked content:// URI's read
        // grant isn't guaranteed to survive past this session otherwise.
        if (uri != null) {
            scope.launch {
                val savedPath = withContext(Dispatchers.IO) {
                    val dir = File(context.filesDir, "client_photos").apply { mkdirs() }
                    val dest = File(dir, "${UUID.randomUUID()}.jpg")
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        dest.outputStream().use { output -> input.copyTo(output) }
                    }
                    dest.toURI().toString()
                }
                photoUri = savedPath
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (clientId == null) "New client" else "Edit client") })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = "Client photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                )
            }
            OutlinedButton(onClick = { photoPicker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                Text(if (photoUri == null) "Add photo" else "Change photo")
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = contactInfo,
                onValueChange = { contactInfo = it },
                label = { Text("Contact info") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                minLines = 6,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "Notes and photos are stored only on this device for now. Cloud sync/backup is coming soon.",
                style = MaterialTheme.typography.bodySmall,
            )

            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    scope.launch {
                        viewModel.save(
                            ClientEntity(
                                id = existing?.id ?: 0,
                                name = name,
                                contactInfo = contactInfo,
                                notes = notes,
                                photoUri = photoUri,
                                createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                            ),
                        )
                        onDone()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save")
            }
        }
    }
}
