package com.example.databasebuilder

import android.app.Application
import com.example.databasebuilder.data.DataRoomDatabase

class DatabaseApplication : Application() {
    val database: DataRoomDatabase by lazy { DataRoomDatabase.getDatabase(this) }
}
