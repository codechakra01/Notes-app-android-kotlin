package com.codechakra.notepad.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codechakra.notepad.R
import com.codechakra.notepad.model.NotesModel
import com.codechakra.notepad.onclicklisteners.NotesClickListener
import com.codechakra.notepad.utils.Utils
import java.sql.Timestamp
import java.text.DateFormat
import java.util.*

class NotesRecyclerAdapter(
    private var notesModelList: List<NotesModel>,
    private val notesClickListener: NotesClickListener, private val activity: Activity
) :
    RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder>() {
    class NotesViewHolder(itemView: View, private val notesClickListener: NotesClickListener) :
        RecyclerView.ViewHolder(itemView), OnClickListener {
        var titleTextView: TextView
        var notesDateCreatedTxt: TextView
        private var menuButton: ImageButton

        init {
            titleTextView = itemView.findViewById(R.id.notesItemTitle)
            notesDateCreatedTxt = itemView.findViewById(R.id.dateCreatedTextView)
            menuButton = itemView.findViewById(R.id.imageButton)
            menuButton.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            when (p0!!.id) {
                R.id.imageButton -> notesClickListener.onMenuClick(p0, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.notes_item_layout, parent, false)
        return NotesViewHolder(view, notesClickListener)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {

        holder.titleTextView.text = notesModelList[position].title
        val ts: Timestamp?

        var putText = ""
        when (Utils.getSavedSortOrder(activity)) {
            Utils.SORT_BY_DATE_CREATED_LATEST -> {
                ts = notesModelList[position].dateCreated?.let { Timestamp(it.toLong()) }
                ts?.let {
                    val date1 = Date(it.time)
                    val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
                    val date: String = dateFormat.format(date1)
                    val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
                    val timeEdited = timeFormat.format(date1)
                    putText = "date created: $date $timeEdited"
                }
            }

            Utils.SORT_BY_DATE_CREATED_OLDEST -> {
                ts = notesModelList[position].dateCreated?.let { Timestamp(it.toLong()) }
                ts?.let {
                    val date1 = Date(it.time)
                    val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
                    val date: String = dateFormat.format(date1)
                    val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
                    val timeEdited = timeFormat.format(date1)
                    putText = "date created: $date $timeEdited"
                }
            }
            Utils.SORT_BY_DATE_EDITED_LATEST -> {
                ts = notesModelList[position].dateLastEdited?.let { Timestamp(it.toLong()) }
                ts?.let {
                    val date1 = Date(it.time)
                    val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
                    val date: String = dateFormat.format(date1)
                    val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
                    val timeEdited = timeFormat.format(date1)
                    putText = "date edited: $date $timeEdited"
                }
            }
            Utils.SORT_BY_DATE_EDITED_OLDEST -> {
                ts = notesModelList[position].dateLastEdited?.let { Timestamp(it.toLong()) }
                ts?.let {
                    val date1 = Date(it.time)
                    val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
                    val date: String = dateFormat.format(date1)
                    val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
                    val timeEdited = timeFormat.format(date1)
                    putText = "date edited: $date $timeEdited"
                }
            }
            else -> {
                ts = notesModelList[position].dateLastEdited?.let { Timestamp(it.toLong()) }
                ts?.let {
                    val date1 = Date(it.time)
                    val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
                    val date: String = dateFormat.format(date1)
                    val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
                    val timeEdited = timeFormat.format(date1)
                    putText = "date edited: $date $timeEdited"
                }
            }


        }
        holder.notesDateCreatedTxt.text = putText


    }

    override fun getItemCount(): Int = notesModelList.size


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<NotesModel>) {
        notesModelList = filterList
        notifyDataSetChanged()
    }
}