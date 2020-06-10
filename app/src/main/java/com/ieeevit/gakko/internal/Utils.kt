package com.ieeevit.gakko.internal

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
        const val SHARED_PREFERENCE_MATERIAL_TYPE = "materialType"
        const val SHARED_PREFERENCE_PROFILE_PIC_URL = "profilePic"
        const val SHARED_PREFERENCE_CURRENT_THREAD = "currentThread"
        const val SHARED_PREFERENCE_CURRENT_TEACHER = "currentTeacher"
        const val SHARED_PREFERENCE_CURRENT_SUBJECT = "currentSubject"
        const val SHARED_PREFERENCE_CURRENT_USER_NAME = "currentUserName"
    }

    fun saveMobile(mobile: String) = editor.putString(SHARED_PREFERENCE_MOBILE, mobile).commit()

    fun retrieveMobile(): String? = sharedPreferences.getString(SHARED_PREFERENCE_MOBILE, null)

    fun saveTeacher(teacherId: String) = editor.putString(SHARED_PREFERENCE_CURRENT_TEACHER, teacherId).commit()

    fun retrieveCurrentTeacher(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_TEACHER, null)

    fun saveCurrentUserName(userName: String) = editor.putString(SHARED_PREFERENCE_CURRENT_USER_NAME, userName).commit()

    fun retrieveCurrentUserName(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_USER_NAME, null)

    fun saveSubject(subject: String) = editor.putString(SHARED_PREFERENCE_CURRENT_SUBJECT, subject).commit()

    fun retrieveCurrentSubject(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_SUBJECT, null)

    fun saveThread(threadId: String) = editor.putString(SHARED_PREFERENCE_CURRENT_THREAD, threadId).commit()

    fun retrieveThread(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_THREAD, null)

    fun saveCurrentClassroom(classroom: String) = editor.putString(SHARED_PREFERENCE_CURRENT_CLASSROOM, classroom).commit()

    fun saveMaterialType(type: String) = editor.putString(SHARED_PREFERENCE_MATERIAL_TYPE, type).commit()

    fun retrieveMaterialType(): String? = sharedPreferences.getString(SHARED_PREFERENCE_MATERIAL_TYPE, null)

    fun retrieveCurrentClassroom(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_CLASSROOM, null)

    fun saveCurrentChat(mobileNumber: String) = editor.putString(SHARED_PREFERENCE_CURRENT_CHAT, mobileNumber).commit()

    fun retrieveCurrentChat(): String? = sharedPreferences.getString(SHARED_PREFERENCE_CURRENT_CHAT, null)

    fun saveTeacherList(teacherList: MutableSet<String>) = editor.putStringSet(SHARED_PREFERNCE_TEACHERLIST, teacherList).commit()

    fun retrieveTeachersList(): MutableSet<String>? = sharedPreferences.getStringSet(SHARED_PREFERNCE_TEACHERLIST, null)

    fun saveProfilePicUrl(url: String) =  editor.putString(SHARED_PREFERENCE_PROFILE_PIC_URL,url).commit()

    fun retrieveProfilePic(): String? = sharedPreferences.getString(SHARED_PREFERENCE_PROFILE_PIC_URL,null)

    fun nukeSharedPrefs() = editor.clear().commit()

}


