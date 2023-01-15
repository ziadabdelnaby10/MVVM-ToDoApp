package com.example.mvvmtodo_listapp.ui.edittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtodo_listapp.data.database.TaskDao
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    //Saved State handle works as safe arguments but in viewModel
    val task = state.get<TaskModel>("Task")

    var taskName = state.get<String>("TaskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state["TaskName"] = value
        }

    var taskImportance = state.get<Boolean>("TaskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state["TaskImportance"] = value
        }

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if (task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportance)
            updatedTask(updatedTask)
        } else {
            val newTask = TaskModel(name = taskName, important = taskImportance)
            createTask(newTask)

        }
    }

    private fun showInvalidInputMessage(s: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(s))
    }

    private fun createTask(newTask: TaskModel) = viewModelScope.launch {
        taskDao.insert(newTask)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(AppConstants.ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(updateTask: TaskModel) = viewModelScope.launch {
        taskDao.updateTask(updateTask)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(AppConstants.EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }

}