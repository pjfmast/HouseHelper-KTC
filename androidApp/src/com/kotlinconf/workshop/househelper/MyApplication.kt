package com.kotlinconf.workshop.househelper

import android.app.Application
import com.kotlinconf.workshop.househelper.di.initApp
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initApp {
            androidContext(this@MyApplication)
        }
    }
}
