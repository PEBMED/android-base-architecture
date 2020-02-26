package com.example.basearch.presentation.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.isValidForGlide(): Boolean = !(this.isDestroyed || this.isFinishing)
