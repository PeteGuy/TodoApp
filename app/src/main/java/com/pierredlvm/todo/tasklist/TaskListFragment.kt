package com.pierredlvm.todo.tasklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.FragmentTaskListBinding
import java.util.*

class TaskListFragment : Fragment()
{
    /*val adapterListener = object : TaskListListener {
        override fun onClickDelete(task: Task)
        {

        }
    }*/

    private lateinit var binding: FragmentTaskListBinding;
    /*val adapterListener = object : TaskListListener{
    override fun onClickAdd(task: Task) { taskList.add(taskList.size,Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"));
            recyclerView.adapter.submitList(taskList.toList());}*/

    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3"));


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTaskListBinding.inflate(inflater);
        val rootView = binding.root;


        return rootView;
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = binding.recyclerView;//view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity);
        val myAdapter = TaskListAdapter();

        val taskDelete:(Task) -> Unit =
            {task ->

                taskList.remove(task);
                /*for(i in 0..taskList.size-1)
                {
                    if(taskList[i].id == task.id)
                    {
                        taskList.removeAt(i);
                    }
                }*/

                myAdapter.submitList(taskList.toList());


            };



        myAdapter.onClickDelete = taskDelete;
        recyclerView.adapter  = myAdapter;

        myAdapter.submitList(taskList.toList());

        val addButton = binding.addTaskButton;//view.findViewById<FloatingActionButton>(R.id.addTaskButton);
        addButton.setOnClickListener { taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"));
            myAdapter.submitList(taskList.toList())
            Log.e("TaskListSize",taskList.size.toString());
        }



    }
}