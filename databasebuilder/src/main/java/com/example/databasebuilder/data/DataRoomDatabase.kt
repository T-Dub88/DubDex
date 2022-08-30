package com.example.databasebuilder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class, AlternateForm::class], version = 1, exportSchema = false)
abstract class DataRoomDatabase: RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

}
