package com.example.loginscreen.TableUsers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class User (
    val loginId:String,
    val fullName:String,
    val phoneNumber:String,
    val Email:String,
    val password:String,
    @PrimaryKey(autoGenerate = true)
    val idL:Int = 0
)