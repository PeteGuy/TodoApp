package com.pierredlvm.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.pierredlvm.todo.R
import com.pierredlvm.todo.SharedViewModel
import com.pierredlvm.todo.databinding.FragmentTaskListBinding
import com.pierredlvm.todo.form.getNavigationResultLiveData

import com.pierredlvm.todo.form.setNavigationResult
import com.pierredlvm.todo.network.Api
import com.pierredlvm.todo.user.SHARED_PREF_TOKEN_KEY
import com.pierredlvm.todo.user.UserInfoFragment

import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch


class TaskListFragment : Fragment()
{

    lateinit var myAdapter:TaskListAdapter;
    private val viewModel: TasksViewModel by viewModels()
    lateinit var avatar: ImageView;

    private val model: SharedViewModel by activityViewModels()




    val adapterListListener = object : TaskListListener{
        override fun onClickDelete(task: Task) {
            lifecycleScope.launch{
                viewModel.deleteTask(task);
            }
        }

        override fun onClickEdit(task: Task) {

            //On set la value de la currently edited

            model.setTaskToEdit(task);

            findNavController().navigate(R.id.action_taskListFragment_to_formFragment)



        }

    }

    var userInfoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

    }


        var launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            //traitement du résultat
            val task = result.data?.getSerializableExtra("task") as? Task
            if (task != null) {
                val oldTask = viewModel.taskList.value.firstOrNull { it.id == task.id }
                if (oldTask != null)
                {   lifecycleScope.launch {
                    viewModel.editTask(task);
                }


                }
                else
                {
                    lifecycleScope.launch {
                        viewModel.addTask(task)
                    }
                }

            };



        }

    public var ADD_TASK_REQUEST_ID: Int = 1;


    private lateinit var binding: FragmentTaskListBinding;





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

        val result = getNavigationResultLiveData<Task>("newTask")

        result?.observe(viewLifecycleOwner) { task ->
            if (task != null) {

                val oldTask = viewModel.taskList.value.firstOrNull { it.id == task.id }
                if (oldTask != null) {
                    lifecycleScope.launch {
                        viewModel.editTask(task);
                    }


                } else {
                    lifecycleScope.launch {
                        viewModel.addTask(task)

                    }
                }
            }
        }

        val recyclerView = binding.recyclerView;//view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity);
        myAdapter = TaskListAdapter(adapterListListener);

        val taskDelete:(Task) -> Unit =
            {task ->



                lifecycleScope.launch{

                    viewModel.deleteTask(task);
                }

            };

        val taskEdit:(Task) -> Unit =
            {task ->

                setNavigationResult(task,"sentTask");
                findNavController().navigate(R.id.action_taskListFragment_to_formFragment)



            };

        recyclerView.adapter  = myAdapter;


        val addButton = binding.addTaskButton;
        addButton.setOnClickListener {
            model.setTaskToEdit(null);
            findNavController().navigate(R.id.action_taskListFragment_to_formFragment)

        }

        binding.logOut.setOnClickListener {
            // Delete token
            PreferenceManager.getDefaultSharedPreferences(Api.appContext).edit {
                putString(SHARED_PREF_TOKEN_KEY,"");
            }
            // Navigate back to start screen
            findNavController().navigate(R.id.action_taskListFragment_to_authenticationFragment)

        }
        // Dans onViewCreated()

        lifecycleScope.launch() {
            viewModel.taskList.collectLatest  { newList ->
                myAdapter.submitList(newList)

            }


        }




    }

    override fun onResume() {

        super.onResume()


        if(PreferenceManager.getDefaultSharedPreferences(Api.appContext).getString(SHARED_PREF_TOKEN_KEY,"") == "")
        {
            // Navigate to authentification
            findNavController().navigate(R.id.action_taskListFragment_to_authenticationFragment)
            return;
        }
        lifecycleScope.launch()
        {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.userInfo.text = "${userInfo.firstName} ${userInfo.lastName}";
            avatar.load(userInfo.avatar){
                // affiche une image par défaut en cas d'erreur:
                error(R.drawable.ic_launcher_background)
            }
            viewModel.loadTasks();
        }

        avatar = binding.avatarImage;

        avatar.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_userInfoFragment)
        }



    }
}