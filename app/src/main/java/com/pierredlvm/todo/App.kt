package com.pierredlvm.todo

import android.app.Application
import com.pierredlvm.todo.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.setUpContext(this);
    }
}