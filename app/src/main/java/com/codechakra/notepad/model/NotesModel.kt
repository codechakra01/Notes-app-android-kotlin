package com.codechakra.notepad.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NotesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String?,
    var text: String?,
    var dateLastEdited: String?,
    val dateCreated:String?
){


}
