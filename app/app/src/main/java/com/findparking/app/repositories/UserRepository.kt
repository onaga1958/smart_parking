package com.findparking.app.repositories

import com.findparking.app.AppExecutors
import com.findparking.app.data.local.AppDatabase
import com.findparking.app.data.local.database.entity.UserEntity
import com.findparking.app.api.UserApi
import javax.inject.Inject

class UserRepository
@Inject constructor(
    private val appDatabase: AppDatabase,
    private val appExecutors: AppExecutors,
    private val userApi: UserApi,
    private val dao: AppDatabase

) {
    fun setUser(user: UserEntity) {
        dao.userDao().insertData(user)
    }
}