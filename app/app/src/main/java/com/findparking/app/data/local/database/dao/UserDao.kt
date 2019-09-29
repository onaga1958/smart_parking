package com.findparking.app.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.findparking.app.data.local.database.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM user LIMIT 1")
    fun getUserLiveData(): LiveData<UserEntity>

    @Query("SELECT * FROM user LIMIT 1")
    fun getUserLiveSync(): UserEntity

    @Query("DELETE FROM user")
    fun nukeTable()
}