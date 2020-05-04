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
        const val SHARED_PREFERENCE_CURRENT_CHAT = "currentChat"
        const val SHARED_PREFERNCE_TEACHERLIST = "TeacherList"
    }

    fun saveMobile(mobile: String) = editor.putString(SHARED_PREFERENCE_MOBILE, mobile).commit()


    fun retrieveMobile(): String? = sharedPreferences.getString(SHARED_PREFERENCE_MOBILE, null)

    fun saveCurrentClassroom(classroom: String) =
        editor.putString(SHARED_PREFERENCE_CURRENT_CLASSROOM, classroom).commit()

    fun retrieveCurrentClassroom(): String? =
        sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_CLASSROOM, null)

    fun saveCurrentChat(mobileNumber: String) =
        editor.putString(SHARED_PREFERENCE_CURRENT_CHAT, mobileNumber).commit()

    fun retrieveCurrentChat(): String? =
        sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_CHAT, null)


    fun saveTeacherList(teacherList: MutableSet<String>) =
        editor.putStringSet(SHARED_PREFERNCE_TEACHERLIST, teacherList).commit()

    fun retrieveTeachersList(): MutableSet<String>? =
        sharedPreferences.getStringSet(SHARED_PREFERNCE_TEACHERLIST, null)

}


