package com.codechakra.notepad.onclicklisteners

import android.view.View

interface NotesClickListener {
    fun onMenuClick(view: View, adapterPosition: Int)
}