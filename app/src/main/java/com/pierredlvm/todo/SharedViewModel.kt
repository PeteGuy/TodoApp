package com.pierredlvm.todo

import androidx.lifecycle.ViewModel
import com.pierredlvm.todo.tasklist.Task

class SharedViewModel  : ViewModel(){
    var testString: String? = null ;
    private var taskToEdit: Task? = null;

    fun getTest():String?
    {
        return testString;
    }

    fun setTest(value:String?)
    {
        testString = value;
    }


    fun getTaskToEdit():Task?
    {
        return taskToEdit;
    }

    fun setTaskToEdit(value:Task?)
    {
        taskToEdit = value;
    }



}