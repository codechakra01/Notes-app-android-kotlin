package com.codechakra.notepad.utils

import android.app.Activity
import android.content.Context

object Utils {
    const val SORT_ORDER = "SORT_PREFS"
    const val SORT_ORDER_NO = "SORT_PREFS_NO"
    const val SORT_BY_DATE_CREATED_LATEST = 0
    const val SORT_BY_DATE_CREATED_OLDEST = 1
    const val SORT_BY_DATE_EDITED_LATEST = 2
    const val SORT_BY_DATE_EDITED_OLDEST = 3
    const val SORT_FROM_A_TO_Z = 4
    const val SORT_FROM_Z_TO_A = 5

    fun getSavedSortOrder(act: Activity): Int {
        val sharedPreferences = act.getSharedPreferences(Utils.SORT_ORDER, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(Utils.SORT_ORDER_NO, 2)
    }
}