package com.codechakra.notepad.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.codechakra.notepad.model.NotesModel
import com.codechakra.notepad.roomdatabase.NotesRoomDatabase

class NotesRepository(application: Application) {
    private var notesDao: NotesDao
    private var allNotes: LiveData<List<NotesModel>>

    init {
        val db: NotesRoomDatabase? = NotesRoomDatabase.getDatabase(application)
        notesDao = db!!.notesDao()
        allNotes = notesDao.getAllNotes()
    }

    fun getAllNotes(): LiveData<List<NotesModel>> = allNotes
    fun getNotes(id: Int): LiveData<NotesModel> = notesDao.getNote(id)
    fun getNotesByLastDate(lastDate: String): LiveData<NotesModel> =
        notesDao.getNotesByDateCreated(lastDate)

    fun insert(notes: NotesModel) {
        NotesRoomDatabase.databaseWriterExecutor.execute {
            notesDao.insert(notes)
        }
    }

    fun update(notes: NotesModel) {
        NotesRoomDatabase.databaseWriterExecutor.execute {
            notesDao.update(notes)
        }
    }

    fun delete(notes: NotesModel) {
        NotesRoomDatabase.databaseWriterExecutor.execute {
            notesDao.delete(notes)
        }
    }

    fun deleteAll() {
        NotesRoomDatabase.databaseWriterExecutor.execute {
            notesDao.deleteAll()
        }
    }
    fun getNotesByDateCreatedNoLiveData(dateCreatedTime: String) =
        notesDao.getNotesByDateCreatedNoLiveData(dateCreatedTime)


    //SORTING FUNCTIONS START


    fun getNotesByDateCreatedOldest(): LiveData<List<NotesModel>> =
        notesDao.getNotesByDateCreatedOldest()

    fun getNotesByDateCreatedLatest(): LiveData<List<NotesModel>> =
        notesDao.getNotesByDateCreatedLatest()


    fun getNotesByLastEditedOldest(): LiveData<List<NotesModel>> =
        notesDao.getNotesByLastEditedOldest()

    fun getNotesByLastEditedLatest(): LiveData<List<NotesModel>> =
        notesDao.getNotesByLastEditedLatest()

    fun getNotesByTitleAtoZ(): LiveData<List<NotesModel>> = notesDao.getNotesByTitleAtoZ()

    fun getNotesByTitleZtoA(): LiveData<List<NotesModel>> = notesDao.getNotesByTitleZtoA()

    //SORTING FUNCTIONS END
}