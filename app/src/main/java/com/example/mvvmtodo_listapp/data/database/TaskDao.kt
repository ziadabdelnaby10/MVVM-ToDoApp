package com.example.mvvmtodo_listapp.data.database

import androidx.room.*
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.util.AppConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskModel)

    // '%' defines that it will search in first or last or middle (anywhere)
    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY important DESC")
    fun getAllTasks(searchQuery: String): Flow<List<TaskModel>>

    //0 -> false in sqlite
    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTasksSortedByName(
        searchQuery: String,
        hideCompleted: Boolean
    ): Flow<List<TaskModel>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDateCreated(
        searchQuery: String,
        hideCompleted: Boolean
    ): Flow<List<TaskModel>>

    fun getTasks(
        query: String,
        sortOrder: AppConstants.SortOrder,
        hideCompleted: Boolean
    ): Flow<List<TaskModel>> = when (sortOrder) {
        AppConstants.SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
        AppConstants.SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
    }

    @Update
    suspend fun updateTask(task: TaskModel)

    @Delete
    suspend fun deleteTask(task: TaskModel)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}