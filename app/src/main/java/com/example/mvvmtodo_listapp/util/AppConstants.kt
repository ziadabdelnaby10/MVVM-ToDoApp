package com.example.mvvmtodo_listapp.util

import android.app.Activity
import androidx.datastore.preferences.preferencesKey

object AppConstants {
    const val USER_PREFERENCES = "user_preferences"
    const val SORT_ORDER = "sort_order"
    const val HIDE_COMPLETED = "hide_completed"
    const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
    const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1

    enum class SortOrder {
        BY_NAME, BY_DATE
    }
}