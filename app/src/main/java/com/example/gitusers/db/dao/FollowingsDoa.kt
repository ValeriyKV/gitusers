package com.example.gitusers.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gitusers.db.entity.Followings
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowingsDoa {

    @Query("delete from Followings where id=:id")
    fun delete(vararg id: Int)

    @Insert
    fun insertAll(vararg followings: Followings)

    @Query("select * from Followings")
    fun getAll(): Flow<List<Followings>>

    @Query("select * from Followings where id=:userId limit 1")
    fun getFollowingByUserId(userId : Int) : Flow<Followings>
}