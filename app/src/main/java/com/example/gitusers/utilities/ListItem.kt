package com.example.gitusers.utilities

import com.example.gitApp.data.User

data class ListItem (
    val user: User = User(),
    val letter : String = "",
    val type : Int){

    companion object {
        const val ITEM_USER = 0
        const val ITEM_LITTER = 1
    }
}