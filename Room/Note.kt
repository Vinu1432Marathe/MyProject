package com.vinayak.semicolon.securefolderhidefiles.Room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "SecureNotes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var content: String
)
// --- 1. Entity: SavedContact.kt ---
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val contactId: String,
    val name: String,
    val number: String
)
