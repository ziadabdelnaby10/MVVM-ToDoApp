package com.example.mvvmtodo_listapp.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodo_listapp.data.model.TaskModel
import com.example.mvvmtodo_listapp.ui.tasks.TaskRecyclerAdapter

@BindingAdapter("android:important")
fun important(imageView: ImageView, isImportant: Boolean?) {
    if (isImportant != null) {
        if (isImportant) {
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.INVISIBLE
        }
    } else {
        imageView.visibility = View.GONE
    }
}

@BindingAdapter("android:finished")
fun finished(textView: TextView, isCompleted: Boolean?) {
    if (isCompleted != null) {
        textView.paint.isStrikeThruText = isCompleted
    }
}

@BindingAdapter("android:listData")
fun listData(recyclerView: RecyclerView, items: List<TaskModel>?) {
    if (items != null){
        (recyclerView.adapter as TaskRecyclerAdapter).apply {
            submitList(items)
        }
    }
}