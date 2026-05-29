package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey
    val email: String,
    val fullName: String,
    val passwordHash: String, // Plaintext or simple base64 for local persistence sandbox
    val createdAt: Long = System.currentTimeMillis()
)
