package com.codechakra.notepad.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codechakra.notepad.data.NotesDao
import com.codechakra.notepad.model.NotesModel
import java.util.concurrent.Executors


@Database(entities = [NotesModel::class], version = 1, exportSchema = false)
abstract class NotesRoomDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao


    companion object{
       const val numberOfThread = 4

       val databaseWriterExecutor =
            Executors.newFixedThreadPool(numberOfThread)
        @Volatile
        var INSTANCE: NotesRoomDatabase? = null

        fun getDatabase(context: Context): NotesRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(NotesRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            Room.databaseBuilder(
                                context.applicationContext,
                                NotesRoomDatabase::class.java ,"notes_database"
                            )
                                //.addCallback(sRoomDatabaseCallBack)
                                .build()
                    }
                }
            }
            return INSTANCE
        }
        private val sRoomDatabaseCallBack: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

    }




}