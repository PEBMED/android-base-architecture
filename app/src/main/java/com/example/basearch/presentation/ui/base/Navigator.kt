package com.example.basearch.presentation.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

object Navigator {
    fun goToActivity(context: Context, newActivityClass: Class<*>, params: Bundle? = null) {
        val intent = Intent(context, newActivityClass)
        params?.let {
            intent.putExtras(params)
        }

        context.startActivity(intent)
    }

    fun goToActivityCleaningBackStack(
        activity: Activity,
        newActivityClass: Class<*>,
        params: Bundle? = null
    ) {
        val intent = Intent(activity, newActivityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        params?.let {
            intent.putExtras(params)
        }

        activity.startActivity(intent)
        activity.finish()
    }

    fun goToActivityWithResult(
        context: Activity,
        newActivityClass: Class<*>,
        requestCode: Int,
        params: Bundle? = null
    ) {
        val intent = Intent(context, newActivityClass)
        params?.let {
            intent.putExtras(params)
        }

        context.startActivityForResult(intent, requestCode)
    }
}