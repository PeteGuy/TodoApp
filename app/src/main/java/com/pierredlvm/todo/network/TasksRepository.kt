package com.pierredlvm.todo.network

import com.pierredlvm.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService





    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateTask(task: Task) : Task?{

        val taskReponse = tasksWebService.update(task,task.id);
        if(taskReponse.isSuccessful)
        {
            return taskReponse.body();


        }
        else
        {
            return null;
        }

    }

    suspend fun deleteTask(id:String):Boolean
    {
        val taskResponse = tasksWebService.delete(id);
        return taskResponse.isSuccessful;

    }

    suspend fun createTask(task:Task):Boolean
    {
        val taskResponse = tasksWebService.create(task);
        return taskResponse.isSuccessful;



    }
}