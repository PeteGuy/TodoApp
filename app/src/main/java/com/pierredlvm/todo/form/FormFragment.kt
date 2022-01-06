package com.pierredlvm.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pierredlvm.todo.R
import com.pierredlvm.todo.SharedViewModel
import com.pierredlvm.todo.databinding.FragmentFormBinding
import com.pierredlvm.todo.tasklist.Task
import java.util.*

class FormFragment : Fragment() {
    private lateinit var binding: FragmentFormBinding;

    private val model: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }

    override fun onStart() {
        super.onStart()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormBinding.inflate(inflater);
        var view = binding.root;



        var vButton = binding.validationButton;
        setNavigationResult(null,"newTask");
        var sentTask = model.getTaskToEdit();
        var test = getNavigationResult<String>("test");






        binding.taskTitle.setText(sentTask?.title);
        binding.taskDescription.setText(sentTask?.description);

        val id = sentTask?.id ?: UUID.randomUUID().toString()

        vButton.setOnClickListener {
            val newTask =Task(id = id, title = binding.taskTitle.text.toString(),description = binding.taskDescription.text.toString())
            setNavigationResult(newTask,"newTask");

            findNavController().popBackStack();
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}


fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun <T> Fragment.setNavigationResultForCurrent(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}