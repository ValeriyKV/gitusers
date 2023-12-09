package com.example.gitusers

import com.example.gitApp.data.User

interface UsersCallback {

    fun onUserClick(user : User)

    fun onUserFollowChange(user:User)

}