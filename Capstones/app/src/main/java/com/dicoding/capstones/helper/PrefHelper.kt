package com.dicoding.capstones.helper

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {

    private var sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun put(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun clear() {
        editor.clear()
            .apply()
    }

    companion object {
        private const val PREF_NAME = "session"
    }
}