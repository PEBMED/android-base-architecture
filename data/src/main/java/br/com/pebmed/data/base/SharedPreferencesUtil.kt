package br.com.pebmed.data.base

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var lastRepoSyncDate: String
}

class SharedPreferencesUtil(context: Context) : PreferenceStorage {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    override var lastRepoSyncDate by StringPreference(prefs, PREF_LAST_REPO_SYNC_DATE, "")

    companion object {
        const val PREFS_NAME = "basearch"
        const val PREF_LAST_REPO_SYNC_DATE = "pref_last_repo_sync_date"
    }
}

class StringPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        preferences.edit { putString(name, value) }
    }
}

class BooleanPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.edit { putBoolean(name, value) }
    }
}

//region Extensions
fun <T> SharedPreferences.getClass(key: String, clazz: Class<T>): T? {

    val stringObject = this.getString(key, null)

    return Gson().fromJson(stringObject, clazz)
}

fun SharedPreferences.Editor.putClass(key: String, clazz: Any?): SharedPreferences.Editor {
    val json: String

    try {
        json = Gson().toJson(clazz)

        this.putString(key, json)
    } catch (e: Exception) {
        Log.e("SharedPreferences", e.message, e)
    } finally {
        return this
    }
}
//endregion