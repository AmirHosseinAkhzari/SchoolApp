package com.example.school.data.remote

import retrofit2.http.GET


interface AstinApi {

    @GET("login")
    suspend fun login()

}