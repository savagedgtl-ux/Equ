package com.equ.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.equ.app.data.local.ClientEntity
import com.equ.app.data.repository.ClientRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientsViewModel(private val repository: ClientRepository) : ViewModel() {

    val clients: StateFlow<List<ClientEntity>> = repository.observeClients()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun save(client: ClientEntity, onSaved: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.saveClient(client)
            onSaved(if (client.id != 0L) client.id else id)
        }
    }

    fun delete(client: ClientEntity) {
        viewModelScope.launch { repository.deleteClient(client) }
    }

    suspend fun getClient(id: Long): ClientEntity? = repository.getClient(id)

    class Factory(private val repository: ClientRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClientsViewModel(repository) as T
    }
}
