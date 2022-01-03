package com.pierredlvm.todo.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController


import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.FragmentAuthenticationBinding
import androidx.navigation.fragment.findNavController
import com.pierredlvm.todo.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater);
        setContentView(binding.root)

    }

}