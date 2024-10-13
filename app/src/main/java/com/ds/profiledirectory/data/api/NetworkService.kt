package com.ds.profiledirectory.data.api

import com.ds.profiledirectory.data.model.UserList
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("api/")
    suspend fun getRandomUserList(
    @Query("results") results: Int
    ):UserList

}