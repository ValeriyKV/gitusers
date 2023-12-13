package com.example.gitusers.fragments.user

import com.example.gitApp.data.User
import com.example.gitusers.db.entity.Followings

class UserModel (var follow : Boolean = false) {
    var user:User= User()
        set(value) {
            field = value
            field.follow = follow
        }

    fun updateFollowing(it: Followings) {
        follow = it.follow == true
        user.follow = follow
    }
}