package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData
import com.ieeevit.gakko.data.models.Material

interface PinboardRepository {
    val materialsList: LiveData<List<Material>>
    suspend fun getMaterialsList(classId: String)
}