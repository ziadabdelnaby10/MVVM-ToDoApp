package com.example.mvvmtodo_listapp.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.shortToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Fragment.shortToast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
}

val <T> T.exhaustive: T
    get() = this