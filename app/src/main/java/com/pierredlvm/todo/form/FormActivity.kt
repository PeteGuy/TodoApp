package com.pierredlvm.todo.form

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.pierredlvm.todo.databinding.ActivityFormBinding
import com.pierredlvm.todo.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(LayoutInflater.from(applicationContext));

        setContentView(binding.root);

        var vButton = binding.validationButton;

        var sentTask =intent?.getSerializableExtra("sentTask") as? Task;

        binding.taskTitle.setText(sentTask?.title);
        binding.taskDescription.setText(sentTask?.description);

        val id = sentTask?.id ?: UUID.randomUUID().toString()

        vButton.setOnClickListener {
            val newTask =Task(id = id, title = binding.taskTitle.text.toString(),description = binding.taskDescription.text.toString())
            intent.putExtra("task",newTask);
            setResult(RESULT_OK, intent)
            finish();
        }


    }
}