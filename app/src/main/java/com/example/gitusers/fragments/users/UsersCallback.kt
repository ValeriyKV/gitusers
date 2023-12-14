package com.example.gitusers.fragments.users

import com.example.gitApp.data.User

interface UsersCallback {

    fun onUserClick(user : User)

    fun onUserFollowChange(user:User)

}