package com.codechakra.notepad.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.codechakra.notepad.data.NotesRepository

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private var allNotes: LiveData<List<NotesModel>>

    companion object {

        lateinit var repository: NotesRepository

        fun insert(notesModel: NotesModel) {
            repository.insert(notesModel)
        }

        fun update(notesModel: NotesModel) {
            repository.update(notesModel)
        }

        fun getNotesByLastDateCreated(lastDateCreated: String) =
            repository.getNotesByLastDate(lastDateCreated)

        fun delete(notesModel: NotesModel) {
            repository.delete(notesModel)
        }

        fun get(id: Int): LiveData<NotesModel> = repository.getNotes(id)


        //SORTING FUNCTIONS START


        fun getNotesByDateCreatedOldest(): LiveData<List<NotesModel>> =
            repository.getNotesByDateCreatedOldest()

        fun getNotesByDateCreatedLatest(): LiveData<List<NotesModel>> =
            repository.getNotesByDateCreatedLatest()


        fun getNotesByLastEditedOldest(): LiveData<List<NotesModel>> =
            repository.getNotesByLastEditedOldest()

        fun getNotesByLastEditedLatest(): LiveData<List<NotesModel>> =
            repository.getNotesByLastEditedLatest()

        fun getNotesByTitleAtoZ(): LiveData<List<NotesModel>> = repository.getNotesByTitleAtoZ()

        fun getNotesByTitleZtoA(): LiveData<List<NotesModel>> = repository.getNotesByTitleZtoA()

        fun getNotesByDateCreatedNoLiveData(dateCreatedTime: String) =
            repository.getNotesByDateCreatedNoLiveData(dateCreatedTime)
        //SORTING FUNCTIONS END

    }

    fun getAllNotes(): LiveData<List<NotesModel>> = allNotes
    fun deleteAllNotes() {
        repository.deleteAll()
    }

    init {
        repository = NotesRepository(application)
        allNotes = repository.getAllNotes()
    }

}