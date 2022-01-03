package com.pierredlvm.todo.user

import android.os.Bundle



import android.provider.Settings.Global.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.FragmentLoginBinding
import com.pierredlvm.todo.form.LoginForm
import com.pierredlvm.todo.network.Api
import kotlinx.coroutines.launch
import androidx.preference.PreferenceManager



const val SHARED_PREF_TOKEN_KEY = "auth_token_key"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(LayoutInflater.from(context))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = binding.root;

        binding.logInButton.setOnClickListener {
            var email = binding.emailLogin.text.toString()
            var password = binding.password.text.toString()


            if( email != "" &&  password != "")
            {
                var loginForm = LoginForm(email,password)
                Log.e("form",loginForm.toString());
                lifecycleScope.launch {
                    var response = Api.userWebService.login(loginForm);
                    if(response.isSuccessful)
                    {
                        var fetchedToken = response.body()?.token;
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
                    }
                    else
                    {
                        Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()

                    }
                }
            }

        }

        return view
    }


}