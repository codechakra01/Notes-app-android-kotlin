package com.codechakra.notepad.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.codechakra.notepad.model.NotesModel

@Dao
interface NotesDao {
    @Insert
    fun insert(notes : NotesModel)

    @Update
    fun update(notes: NotesModel)

    @Query("SELECT * FROM notes_table ORDER BY dateLastEdited ASC")
    fun getAllNotes() : LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes_table WHERE id == :id")
    fun getNote(id: Int) : LiveData<NotesModel>

    @Query("SELECT * FROM notes_table WHERE dateCreated == :dateCreatedTime")
    fun getNotesByDateCreated(dateCreatedTime: String) : LiveData<NotesModel>

    //SORTING FUNCTIONS START
    @Query("SELECT * FROM notes_table ORDER BY dateCreated ASC")
    fun getNotesByDateCreatedOldest(): LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes_table ORDER BY dateCreated DESC")
    fun getNotesByDateCreatedLatest(): LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes_table ORDER BY dateCreated ASC")
    fun getNotesByLastEditedOldest(): LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes_table ORDER BY dateCreated DESC")
    fun getNotesByLastEditedLatest(): LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes_table ORDER BY title ASC")
    fun getNotesByTitleAtoZ():LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes_table ORDER BY title DESC")
    fun getNotesByTitleZtoA():LiveData<List<NotesModel>>

    //SORTING FUNCTIONS END

    @Query("SELECT * FROM notes_table WHERE dateCreated == :dateCreatedTime")
    fun getNotesByDateCreatedNoLiveData(dateCreatedTime: String) : NotesModel

    @Query("DELETE  FROM notes_table")
    fun deleteAll()

    @Delete
    fun delete(notes: NotesModel)

}