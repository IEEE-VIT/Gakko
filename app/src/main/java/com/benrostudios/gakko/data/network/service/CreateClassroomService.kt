package com.benrostudios.gakko.data.network.service

import com.benrostudios.gakko.data.network.response.GetClassroomIdResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CreateClassroomService {

    @GET("get-class-id")
    suspend fun getClassroomId(): Response<GetClassroomIdResponse>

    companion object {
        operator fun invoke(): CreateClassroomService {
            return Retrofit.Builder()
                .baseUrl("https://gakko-service.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CreateClassroomService::class.java)
        }
    }


}