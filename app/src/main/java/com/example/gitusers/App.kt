package com.example.gitusers

import android.app.Application
import android.content.Context
import com.example.gitusers.db.Database
import java.util.concurrent.Executors

class App : Application() {

    companion object{
        private lateinit var context:Context
        fun getAppContext():Context = context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Executors.newSingleThreadExecutor().submit({
            Database.createDatabase()
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        Database.createDatabase().close()
    }
}