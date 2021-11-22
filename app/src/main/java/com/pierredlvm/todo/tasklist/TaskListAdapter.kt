package com.pierredlvm.todo.tasklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pierredlvm.todo.R
import com.pierredlvm.todo.databinding.FragmentTaskListBinding
import com.pierredlvm.todo.databinding.ItemTaskBinding


object TasksDiffCallback : DiffUtil.ItemCallback<Task>()
{
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {



            return oldItem.id == newItem.id;

    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {

        return (oldItem.title == newItem.title && oldItem.description == newItem.description);
    }

}

/*interface TaskListListener
{
    fun onClickDelete(task:Task);
}*/

class TaskListAdapter() : ListAdapter<Task,TaskListAdapter.TaskViewHolder>(TasksDiffCallback)
{
    var onClickDelete: ((Task) -> Unit)? = null
    private lateinit var binding: ItemTaskBinding;
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(task:Task) {

            val textView = binding.taskTitle;

            textView.setText(task.title+"\n"+task.description);
            binding.deleteButton.setOnClickListener { onClickDelete?.invoke(task) };
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        //val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false);
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context));
        val itemView = binding.root;
        val taskViewHolder = TaskViewHolder(itemView);

        return taskViewHolder;

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position]);



       // binding.deleteButton.setOnClickListener { onClickDelete?.invoke(currentList[position]) };
    }


}
