package com.example.loginscreen.TableUsers.UserDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.loginscreen.TableUsers.User


@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user:User)
    @Delete
    suspend fun deleteUser(user:User)

    @Query("SELECT * FROM user WHERE loginId = :userId")
    fun getUserByLoginId(userId: String?): User?
}