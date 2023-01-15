package com.example.mvvmtodo_listapp.data.database

import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.example.mvvmtodo_listapp.util.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.createDataStore(AppConstants.USER_PREFERENCES)

    val preferencesFlow = dataStore.data
        .catch {
                exception ->
            if(exception is IOException){
                Log.e(TAG , "Error Reading From Pref" , exception)
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { pref ->
        val sortOrder = AppConstants.SortOrder.valueOf(
            pref[PreferencesKeys.SORT_ORDER_KEY] ?: AppConstants.SortOrder.BY_DATE.name
        )
        val hideCompleted = pref[PreferencesKeys.HIDE_COMPLETED_KEY] ?: false
        FilterPreferences(sortOrder, hideCompleted)
    }

    suspend fun updateSortOrder(sortOrder: AppConstants.SortOrder){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER_KEY] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED_KEY] = hideCompleted
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER_KEY = preferencesKey<String>(AppConstants.SORT_ORDER)
        val HIDE_COMPLETED_KEY = preferencesKey<Boolean>(AppConstants.HIDE_COMPLETED)
    }

    data class FilterPreferences(val sortOrder: AppConstants.SortOrder, val hideCompleted: Boolean)
}

