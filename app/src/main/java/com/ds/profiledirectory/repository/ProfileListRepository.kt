package com.ds.profiledirectory.repository

import com.ds.profiledirectory.data.api.NetworkService
import com.ds.profiledirectory.data.model.UserList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileListRepository @Inject constructor(private val networkService: NetworkService) {

    fun getUserList(noOfUser: Int): Flow<UserList>{
        return flow { emit(networkService.getRandomUserList(noOfUser)) }
    }
}