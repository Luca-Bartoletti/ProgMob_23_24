package com.example.betterpath.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.betterpath.data.PathData
import com.example.betterpath.data.PathDataDao
import com.example.betterpath.data.PathHistory
import com.example.betterpath.data.PathHistoryDao

@Database(
    entities = [
        PathHistory::class,
        PathData::class
    ],
    version = 2,
    // todo rimuovere?
    exportSchema = false
)

abstract class MyAppDatabase : RoomDatabase() {
    abstract fun pathHistoryDao(): PathHistoryDao?
    abstract fun pathDataDao(): PathDataDao?

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        fun getDatabase(context: Context): MyAppDatabase? {
            if (INSTANCE == null) {
                synchronized(MyAppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = androidx.room.Room.databaseBuilder(
                            context.applicationContext,
                            MyAppDatabase::class.java, "mobility_database"
                        )
                            // how to add a migration
                            .addMigrations(

                            )
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         * If you want to populate the database only when the database is created for the 1st time,
         * override MyRoomDatabase.Callback()#onCreate
         */
        private val roomDatabaseCallback: Callback =
            object : Callback() {
            }
    }

}