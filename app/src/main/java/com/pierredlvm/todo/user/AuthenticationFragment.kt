package com.pierredlvm.todo.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.ActivityAuthenticationBinding
import com.pierredlvm.todo.databinding.FragmentAuthenticationBinding


class AuthenticationFragment : Fragment() {
    private lateinit var binding : FragmentAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAuthenticationBinding.inflate(layoutInflater);





    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = binding.root//inflater.inflate(R.layout.fragment_authentication,container,false)
        binding.logIn.setOnClickListener {

            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment);
        }
        return view

    }


}

