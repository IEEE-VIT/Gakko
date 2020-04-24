package com.benrostudios.gakko.internal

import android.content.Context
import android.content.SharedPreferences

class Utils(
    private val context: Context
) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor

    init {
        editor = sharedPreferences.edit()
    }

    companion object {
        const val SHARED_PREFERENCE_MOBILE = "mobile"
        const val SHARED_PREFERENCE_CURRENT_CLASSROOM = "currentClassroom"
    }

    fun saveMobile(mobile: String) = editor.putString(SHARED_PREFERENCE_MOBILE, mobile).commit()


    fun retrieveMobile(): String? = sharedPreferences.getString(SHARED_PREFERENCE_MOBILE, null)

    fun saveCurrentClassroom(classroom: String) = editor.putString(SHARED_PREFERENCE_CURRENT_CLASSROOM,classroom).commit()

    fun retrieveCurrentClassroom(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_CLASSROOM,null)

}

