package com.example.mvvmtodo_listapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [TaskModel::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(private val database: Provider<TaskDatabase> ,
    @ApplicationScope private val applicationScope : CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(TaskModel("Wash the dishes"))
                dao.insert(TaskModel("Do the laundry"))
                dao.insert(TaskModel("Buy groceries", important = true))
                dao.insert(TaskModel("Prepare food", completed = true))
                dao.insert(TaskModel("Call mom"))
                dao.insert(TaskModel("Visit grandma", completed = true))
                dao.insert(TaskModel("Repair my bike"))
                dao.insert(TaskModel("Call Elon Musk"))
            }
        }
    }
}