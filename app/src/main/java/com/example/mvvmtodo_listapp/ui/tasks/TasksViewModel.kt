package com.example.mvvmtodo_listapp.ui.tasks

import androidx.lifecycle.*
import com.example.mvvmtodo_listapp.data.database.PreferencesManager
import com.example.mvvmtodo_listapp.data.database.TaskDao
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    state: SavedStateHandle
) : ViewModel() {
    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskFlow = combine(
        searchQuery.asFlow(), preferencesFlow
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (query, filterPreference) ->
        taskDao.getTasks(query, filterPreference.sortOrder, filterPreference.hideCompleted)
    }

    val tasks = taskFlow.asLiveData()

    fun onSortedOrderSelected(sortOrder: AppConstants.SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClicked(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(item: TaskModel) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(item))
    }

    fun onTaskCheckedChanged(taskModel: TaskModel, checked: Boolean) = viewModelScope.launch {
        taskDao.updateTask(taskModel.copy(completed = checked))
    }

    fun onTaskSwiped(task: TaskModel) = viewModelScope.launch {
        taskDao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: TaskModel) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTask() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            AppConstants.ADD_TASK_RESULT_OK -> {
                showTaskSavedConfirmationMessage("Task added")
            }
            AppConstants.EDIT_TASK_RESULT_OK -> {
                showTaskSavedConfirmationMessage("Task updated")
            }
        }
    }

    private fun showTaskSavedConfirmationMessage(s: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(s))
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: TaskModel) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: TaskModel) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }
}