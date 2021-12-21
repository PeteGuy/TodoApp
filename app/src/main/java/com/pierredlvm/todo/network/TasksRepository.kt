package com.pierredlvm.todo.network

import com.pierredlvm.todo.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    //private val _taskList = MutableStateFlow<List<Task>>(value = emptyList())
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    //public val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    suspend fun refresh() {
        /*// Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            if (fetchedTasks != null) _taskList.value = fetchedTasks
        }*/
    }

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateTask(task: Task) : Task?{

        val taskReponse = tasksWebService.update(task,task.id);
        if(taskReponse.isSuccessful)
        {
            return taskReponse.body();
            /*val updatedTask = taskReponse.body()
            if(updatedTask != null)
            {
                val oldTask = taskList.value.firstOrNull { it.id == updatedTask.id }
                if (oldTask != null)
                {
                    _taskList.value = taskList.value - oldTask + updatedTask
                }

            }*/

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