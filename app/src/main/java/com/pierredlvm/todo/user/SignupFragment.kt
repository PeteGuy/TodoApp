package com.pierredlvm.todo.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.FragmentSignupBinding
import com.pierredlvm.todo.form.LoginForm
import com.pierredlvm.todo.form.SignUpForm
import com.pierredlvm.todo.network.Api
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignupBinding.inflate(LayoutInflater.from(context));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = binding.root;

        binding.signUpButton.setOnClickListener {
            var signUpForm = SignUpForm(
            binding.firstNameInput.text.toString(),
            binding.lastNameInput.text.toString(),
            binding.emailInput.text.toString(),
            binding.passwordInput.text.toString(),
            binding.passwordConfirmationInput.text.toString()

            )

            Log.e("form",signUpForm.toString());

            if( signUpForm.firstname != "" &&  signUpForm.lastname != "" && signUpForm.email != "" && signUpForm.password != "" && signUpForm.password_confirmation != "")
            {

                lifecycleScope.launch {
                    var response = Api.userWebService.signUp(signUpForm);
                    Log.e("response",response.code().toString());
                    if(response.isSuccessful)
                    {
                        var fetchedToken = response.body()?.token;
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
                        }
                        findNavController().navigate(R.id.action_signupFragment_to_taskListFragment)

                    }
                    else
                    {
                        Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()

                    }
                }
            }

        }

        return view;
    }


}