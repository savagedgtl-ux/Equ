package com.equ.app.data.repository

import com.equ.app.data.local.ClientDao
import com.equ.app.data.local.ClientEntity
import kotlinx.coroutines.flow.Flow

/**
 * Client profiles are stored locally only for now (Room). Syncing to a
 * backend so a therapist can use the app on more than one device is
 * planned once the Supabase project is wired up — see ARCHITECTURE.md.
 */
class ClientRepository(private val dao: ClientDao) {
    fun observeClients(): Flow<List<ClientEntity>> = dao.observeAll()

    suspend fun getClient(id: Long): ClientEntity? = dao.getById(id)

    suspend fun saveClient(client: ClientEntity): Long = dao.upsert(client)

    suspend fun updateClient(client: ClientEntity) = dao.update(client)

    suspend fun deleteClient(client: ClientEntity) = dao.delete(client)
}
