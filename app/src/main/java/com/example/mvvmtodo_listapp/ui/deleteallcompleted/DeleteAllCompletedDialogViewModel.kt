package com.example.mvvmtodo_listapp.ui.deleteallcompleted

import androidx.lifecycle.ViewModel
import com.example.mvvmtodo_listapp.data.database.TaskDao
import com.example.mvvmtodo_listapp.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedDialogViewModel @Inject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}