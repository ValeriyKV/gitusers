package com.example.gitusers.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Followings")
data class Followings (
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "follow") val follow: Boolean?
)