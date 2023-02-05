package com.codechakra.notepad

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.codechakra.notepad.adapters.NotesRecyclerAdapter
import com.codechakra.notepad.databinding.ActivityMainBinding
import com.codechakra.notepad.model.NotesModel
import com.codechakra.notepad.model.NotesViewModel
import com.codechakra.notepad.onclicklisteners.NotesClickListener
import com.codechakra.notepad.utils.Utils
import java.io.OutputStream


class MainActivity : AppCompatActivity(), NotesClickListener {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var notesList: List<NotesModel>
    private var liveDataNotesModel: LiveData<List<NotesModel>>? = null
    private lateinit var noteViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            startActivity(intent)
        }
        mainBinding.addNotesBt.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            startActivity(intent)
        }
        noteViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(NotesViewModel::class.java)

        mainBinding.floatingActionButton.setColorFilter(Color.WHITE)
        mainBinding.closeSortCardBt.setOnClickListener {
            mainBinding.sortMenuCard.visibility = View.GONE
        }
        sortOrderClickListeners()
//        val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            val myData : Intent? = it.data
//
//            myData?.let {it1->
//
//                val fileContent = it1.getStringExtra("file_content")
//                val fileUri : Uri? = it1.data
//                try {
//                    val outputStream : OutputStream? = fileUri?.let { it2 ->
//                        contentResolver.openOutputStream(
//                            it2
//                        )
//                    }
//                    outputStream?.write(fileContent!!.toByteArray())
//                    outputStream?.close()
//                }catch (e :Exception){
//                }
//            }
//
//
//        }
//        val createFileIntent = Intent(Intent.ACTION_CREATE_DOCUMENT)
//        createFileIntent.type = "text/plain"
//        createFileIntent.putExtra(Intent.EXTRA_TITLE,"niyog.txt")
//        createFileIntent.putExtra("file_content","hello this is a text file")
//        requestActivity.launch(createFileIntent)

    }

    override fun onResume() {
        getNotesWithPrefs()
        super.onResume()
    }

    override fun onMenuClick(view: View, adapterPosition: Int) {
        val popupMenu = PopupMenu(
            view.context,
            view
        )
        popupMenu.inflate(R.menu.notes_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.openNotes -> openEditorActivity(adapterPosition)
                R.id.editNotes -> openEditorActivity(adapterPosition)
                R.id.deleteNotes -> deleteNotes(adapterPosition)
            }
            true
        }
        popupMenu.show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
        // below line is to call set on query text listener method.
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }



    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<NotesModel> = ArrayList()

        // running a for loop to compare elements.
        for (item in notesList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.title?.lowercase()!!.contains(text.lowercase()) || item.text?.lowercase()!!.contains(text.lowercase())) {
                // if the item is matched we are
                // adding it to our filtered list.

                filteredList.add(item)
            }
        }
        if (filteredList.isNotEmpty()) {
            notesAdapter.filterList(filteredList)
        }
    }

    private fun sortOrderClickListeners(){
        mainBinding.sortByDateCreatedNewestRel.setOnClickListener {
            setRadioButtonUnchecked()
            mainBinding.creationDateNewestRB.isChecked = true
            saveSortPrefs(Utils.SORT_BY_DATE_CREATED_LATEST)
            getNotesWithPrefs()
            Toast.makeText(this,"Sorted successfully!",Toast.LENGTH_SHORT)
                .show()
        }
        mainBinding.sortByDateCreatedOldestRel.setOnClickListener {
            setRadioButtonUnchecked()

            saveSortPrefs(Utils.SORT_BY_DATE_CREATED_OLDEST)
            getNotesWithPrefs()
            Toast.makeText(this,"Sorted successfully!",Toast.LENGTH_SHORT)
                .show()
        }
        mainBinding.sortByEditDateNewestRel.setOnClickListener {
            setRadioButtonUnchecked()

            saveSortPrefs(Utils.SORT_BY_DATE_EDITED_LATEST)
            getNotesWithPrefs()
            Toast.makeText(this,"Sorted successfully!",Toast.LENGTH_SHORT)
                .show()
        }
        mainBinding.sortByEditDateOldestRel.setOnClickListener {
            setRadioButtonUnchecked()

            saveSortPrefs(Utils.SORT_BY_DATE_EDITED_OLDEST)
            getNotesWithPrefs()
            Toast.makeText(this,"Sorted successfully!",Toast.LENGTH_SHORT)
                .show()
        }
        mainBinding.sortByTitleAtoZRel.setOnClickListener {
            setRadioButtonUnchecked()

            saveSortPrefs(Utils.SORT_FROM_A_TO_Z)

            getNotesWithPrefs()
            Toast.makeText(this,"Sorted successfully!",Toast.LENGTH_SHORT)
                .show()

        }
        mainBinding.sortByTitleZtoARel.setOnClickListener {
            setRadioButtonUnchecked()

            saveSortPrefs(Utils.SORT_FROM_Z_TO_A)

            getNotesWithPrefs()
            Toast.makeText(this,"Sorted successfully!",Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun setRadioButtonUnchecked(){
        mainBinding.creationDateNewestRB.isChecked = false
        mainBinding.creationDateOldestRB.isChecked = false
        mainBinding.editDateFromNewest.isChecked = false
        mainBinding.editDateFromOldest.isChecked = false
        mainBinding.titleAtoZ.isChecked = false
        mainBinding.titleZtoA.isChecked = false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortButton -> {
                mainBinding.sortMenuCard.visibility = View.VISIBLE
            }



        }

        return super.onOptionsItemSelected(item)
    }

    private fun openEditorActivity(position: Int) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("lastDateCreated", notesList[position].dateCreated)
        startActivity(intent)
    }

    private fun deleteNotes(position: Int) {
        val title = notesList[position].title
        AlertDialog.Builder(this).setTitle("Do you want to delete?")
            .setMessage("Notes Named: $title")
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("Yes") { _, _ ->
                NotesViewModel.delete(notesList[position])
                Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
            .show()
    }

    private fun getNotesWithPrefs() {
        liveDataNotesModel?.let {
            if (it.hasObservers()) {
                it.removeObservers(this)
            }
        }


        when (Utils.getSavedSortOrder(this)) {
            Utils.SORT_BY_DATE_CREATED_LATEST -> {
                setRadioButtonUnchecked()
                mainBinding.creationDateNewestRB.isChecked = true
                liveDataNotesModel = NotesViewModel.getNotesByDateCreatedLatest()
                liveDataNotesModel?.observe(this) {
                    notesList = it
                    notesAdapter =  NotesRecyclerAdapter(it, this,this)
                    mainBinding.notesRecycler.adapter = notesAdapter
                }
            }
            Utils.SORT_BY_DATE_CREATED_OLDEST -> {
                setRadioButtonUnchecked()
                mainBinding.creationDateOldestRB.isChecked = true
                liveDataNotesModel = NotesViewModel.getNotesByDateCreatedOldest()
                liveDataNotesModel?.observe(this) {
                    notesList = it
                    notesAdapter =  NotesRecyclerAdapter(it, this,this)
                    mainBinding.notesRecycler.adapter = notesAdapter
                }
            }
            Utils.SORT_BY_DATE_EDITED_LATEST -> {
                setRadioButtonUnchecked()
                mainBinding.editDateFromNewest.isChecked = true
                liveDataNotesModel = NotesViewModel.getNotesByLastEditedLatest()
                liveDataNotesModel?.observe(this) {
                    notesList = it
                    notesAdapter =  NotesRecyclerAdapter(it, this,this)
                    mainBinding.notesRecycler.adapter = notesAdapter
                }
            }
            Utils.SORT_BY_DATE_EDITED_OLDEST -> {
                setRadioButtonUnchecked()
                mainBinding.editDateFromOldest.isChecked = true
                liveDataNotesModel = NotesViewModel.getNotesByLastEditedOldest()
                liveDataNotesModel?.observe(this) {
                    notesList = it
                    notesAdapter =  NotesRecyclerAdapter(it, this,this)
                    mainBinding.notesRecycler.adapter = notesAdapter                }
            }
            Utils.SORT_FROM_A_TO_Z -> {
                setRadioButtonUnchecked()
                mainBinding.titleAtoZ.isChecked = true
                liveDataNotesModel = NotesViewModel.getNotesByTitleAtoZ()
                liveDataNotesModel?.observe(this) {
                    notesList = it
                    notesAdapter =  NotesRecyclerAdapter(it, this,this)
                    mainBinding.notesRecycler.adapter = notesAdapter                }
            }
            Utils.SORT_FROM_Z_TO_A -> {
                setRadioButtonUnchecked()
                mainBinding.titleZtoA.isChecked = true
                liveDataNotesModel = NotesViewModel.getNotesByTitleZtoA()
                liveDataNotesModel?.observe(this) {
                    notesList = it
                    notesAdapter =  NotesRecyclerAdapter(it, this,this)
                    mainBinding.notesRecycler.adapter = notesAdapter                }
            }
        }

    }

    private fun saveSortPrefs(number: Int) {
        val sharedPreferences = getSharedPreferences(Utils.SORT_ORDER, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(Utils.SORT_ORDER_NO, number)
        editor.apply()
    }


}