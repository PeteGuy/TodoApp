package com.pierredlvm.todo.tasklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pierredlvm.todo.network.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {

    private val repository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(value = emptyList())
    public val taskList: StateFlow<List<Task>> = _taskList



    fun loadTasks() {
        viewModelScope.launch {
            val fetchedTasks = repository.loadTasks()
            if(fetchedTasks != null)
            {
                _taskList.value = fetchedTasks;
            }
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            Log.e("testDelete","attempted to delete")
            if(repository.deleteTask(task.id))
            {

                val taskToDelete = taskList.value.firstOrNull { it.id == task.id }
                if(taskToDelete != null)
                {
                    _taskList.value =  _taskList.value-taskToDelete;
                }
            }
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            if (repository.createTask(task)) {

                if (task != null) {
                    _taskList.value = _taskList.value + task;
                }
            }
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = repository.updateTask(task);
            if(updatedTask != null)
            {
                val oldTask = taskList.value.firstOrNull { it.id == updatedTask.id }
                if (oldTask != null)
                {
                    _taskList.value = taskList.value - oldTask + updatedTask
                }

            }
        }


    }
}