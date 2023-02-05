package com.codechakra.notepad

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.codechakra.notepad.databinding.ActivityEditorBinding
import com.codechakra.notepad.model.NotesModel
import com.codechakra.notepad.model.NotesViewModel

class EditorActivity : AppCompatActivity() {
    private lateinit var editorBinding: ActivityEditorBinding
    var dateCreated: String? = null
    var lastEdited: String? = null
    private  var liveData :LiveData<NotesModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editorBinding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(editorBinding.root)
        setSupportActionBar(editorBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

//        val noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
//            NotesViewModel::class.java
//        )
        //= System.currentTimeMillis().toString()

        if (intent.hasExtra("lastDateCreated")) {

             liveData =
                NotesViewModel.getNotesByLastDateCreated(intent.getStringExtra("lastDateCreated")!!)
            liveData!!.observe(this) {
                dateCreated = it.dateCreated
                lastEdited = it.dateLastEdited
                editorBinding.titleEdit.text.clear()
                editorBinding.notesEdit.text.clear()
                editorBinding.titleEdit.append(it.title)
                editorBinding.notesEdit.append(it.text)

            }
        } else {
            dateCreated = System.currentTimeMillis().toString()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.editor_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveNotes -> {
                saveNoteFromMenu()
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT)
                    .show()
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNoteFromMenu() {
        val title = editorBinding.titleEdit.text.toString()
        val notesText = editorBinding.notesEdit.text.toString()
        lastEdited = System.currentTimeMillis().toString()
        if (dateCreated == null) {
            dateCreated = System.currentTimeMillis().toString()
        }
        if (title.isNotEmpty() && notesText.isNotEmpty()) {
            val notes = NotesModel(
                title = title, text = notesText, dateLastEdited = lastEdited,
                dateCreated = dateCreated
            )
            liveData?.let {
                if (it.hasObservers()){
                    it.removeObservers(this)
                }
            }

            NotesViewModel.getNotesByLastDateCreated(notes.dateCreated!!).observe(this) {
                if (it == null) {
                    NotesViewModel.insert(notes)
                } else {
                    if (it.text.toString().length != notesText.length) {
                        it.text = notesText
                        it.dateLastEdited = lastEdited
                    }
                    if (it.title.toString().length != title.length) {
                        it.title = title
                        it.dateLastEdited = lastEdited
                    }
                    NotesViewModel.update(it)
                }


            }
            Log.d("lastCreated", "saveNoteFromMenu: $lastEdited ")
            Log.d("dateCreated", "saveNoteFromMenu: $dateCreated ")

            Toast.makeText(this, "Notes saved successfully", Toast.LENGTH_SHORT)
                .show()


        }

        closeKeyBoard()

    }

    object HideKey {

        fun hideSoftKeyBoard(context: Context, view: View) {
            try {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                // TODO: handle exception
                e.printStackTrace()
            }

        }
    }

    private fun closeKeyBoard() {
        try {
            HideKey.hideSoftKeyBoard(this, editorBinding.root)
            editorBinding.notesEdit.clearFocus()
            editorBinding.titleEdit.clearFocus()
        } catch (e: Exception) {
            Log.d("KEY", "closeKeyBoard: $e")
        }

    }

    private fun saveNotes() {
        val title = editorBinding.titleEdit.text.toString()
        val notesText = editorBinding.notesEdit.text.toString()
        if (title.isNotEmpty() && notesText.isNotEmpty()) {
            val notes = NotesModel(
                title = title,
                text = notesText,
                dateLastEdited = System.currentTimeMillis().toString(),
                dateCreated = dateCreated
            )
           liveData?.let {
               if (it.hasObservers()){
                   it.removeObservers(this)
               }
           }
            NotesViewModel.getNotesByLastDateCreated(notes.dateCreated!!).observe(this) {
                if (it == null) {
                    NotesViewModel.insert(notes)
                } else {
                    if (it.text.toString().length != notesText.length) {
                        it.text = notesText
                        it.dateLastEdited = System.currentTimeMillis().toString()

                    }
                    if (it.title.toString().length != title.length) {
                        it.title = title
                        it.dateLastEdited = System.currentTimeMillis().toString()
                    }
                    NotesViewModel.update(it)

                }


            }
        }
    }

    override fun onPause() {
//        val inputMethodManager: InputMethodManager =
//            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
//
//        inputMethodManager.hideSoftInputFromWindow(editorBinding.notesEdit.windowToken, 0)
        if (liveData?.hasActiveObservers() == true){
            liveData!!.removeObservers(this)
        }

        saveNotes()
        super.onPause()

    }
}