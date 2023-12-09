package com.example.gitusers.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gitusers.App
import com.example.gitusers.db.dao.FollowingsDoa
import com.example.gitusers.db.entity.Followings
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Followings::class], version = 1)
abstract class Database : RoomDatabase() {

    companion object{
        private lateinit var database: com.example.gitusers.db.Database

        @OptIn(InternalCoroutinesApi::class)
        fun createDatabase(): com.example.gitusers.db.Database {
            if(!::database.isInitialized) {
                synchronized(Database::class.java) {
                    database = Room.databaseBuilder(
                        App.getAppContext(),
                        com.example.gitusers.db.Database::class.java, "database-name"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }

            return database
        }
    }

    abstract fun followingsDoa(): FollowingsDoa

}