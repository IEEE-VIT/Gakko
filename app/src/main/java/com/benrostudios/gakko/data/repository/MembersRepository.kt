package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Members

interface MembersRepository {
    val teachersList: LiveData<List<Members>>
    val studentsList: LiveData<List<Members>>
    suspend fun getMembers(phoneList: List<String> , type: String)
}