package com.example.loginscreen.TableUsers.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.loginscreen.TableUsers.User
import com.example.loginscreen.TableUsers.UserDao.UserDao

@Database(
    entities = [User::class],
    version = 1
)
abstract class DBUser:RoomDatabase() {
    abstract val dao:UserDao
}