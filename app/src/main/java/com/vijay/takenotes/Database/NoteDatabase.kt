package com.vijay.takenotes.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vijay.takenotes.Notes.Note
import com.vijay.takenotes.Notes.NoteDao

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object{
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(ctx : Context): NoteDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, NoteDatabase::class.java, "note_database")
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }
}