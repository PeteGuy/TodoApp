package com.pierredlvm.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.pierredlvm.todo.network.Api
import com.pierredlvm.todo.user.SHARED_PREF_TOKEN_KEY

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)


    }
}