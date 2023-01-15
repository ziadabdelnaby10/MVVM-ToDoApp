package com.example.mvvmtodo_listapp.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodo_listapp.R
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.databinding.FragmentTasksBinding
import com.example.mvvmtodo_listapp.ui.base.BaseFragment
import com.example.mvvmtodo_listapp.util.AppConstants
import com.example.mvvmtodo_listapp.util.exhaustive
import com.example.mvvmtodo_listapp.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : BaseFragment<FragmentTasksBinding>(), TaskOnClickListener {

    private val taskViewModel by viewModels<TasksViewModel>()

    private lateinit var searchView: SearchView

    override val layoutId: Int
        get() = R.layout.fragment_tasks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //work
        init()
        setupMenu()

    }

    private fun init() {
        getViewDataBinding().apply {
            this.viewModel = taskViewModel
            rvTasks.adapter = TaskRecyclerAdapter(this@TasksFragment)
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task =
                        (rvTasks.adapter as TaskRecyclerAdapter).currentList[viewHolder.adapterPosition]
                    taskViewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(rvTasks)

            fabAddTask.setOnClickListener {
                taskViewModel.onAddNewTask()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            taskViewModel.onAddEditResult(result)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            taskViewModel.tasksEvent.collect { event ->
                when (event) {
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo") {
                                taskViewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditFragment(
                            "Edit Task",
                            event.task
                        )
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditFragment(
                            "Add New Task",
                            null
                        )
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action =
                            TasksFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }


    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_task_fragment, menu)
                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView

                val pendingQuery = taskViewModel.searchQuery.value
                if (pendingQuery != null && pendingQuery.isNotEmpty()) {
                    searchItem.expandActionView()
                    searchView.setQuery(pendingQuery , false)
                }

                searchView.onQueryTextChanged {
                    //update Search Query
                    taskViewModel.searchQuery.value = it
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                        taskViewModel.preferencesFlow.first().hideCompleted
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_sort_by_name -> {
                        taskViewModel.onSortedOrderSelected(AppConstants.SortOrder.BY_NAME)
                        true
                    }
                    R.id.action_sort_by_date_created -> {
                        taskViewModel.onSortedOrderSelected(AppConstants.SortOrder.BY_DATE)
                        true
                    }
                    R.id.action_hide_completed_tasks -> {
                        menuItem.isChecked = !menuItem.isChecked
                        taskViewModel.onHideCompletedClicked(menuItem.isChecked)
                        true
                    }
                    R.id.action_delete_all_completed_tasks -> {
                        taskViewModel.onDeleteAllCompletedClick()
                        true
                    }
                    else -> true
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onItemClick(position: Int, item: TaskModel) {
        taskViewModel.onTaskSelected(item)
    }

    override fun onCheckBoxClick(taskModel: TaskModel, isChecked: Boolean) {
        taskViewModel.onTaskCheckedChanged(taskModel, isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}