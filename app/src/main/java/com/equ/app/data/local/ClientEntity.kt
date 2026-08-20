package com.equ.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val contactInfo: String,
    val notes: String,
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)
