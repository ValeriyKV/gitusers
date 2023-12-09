package com.example.gitusers

import com.example.gitApp.data.User
import com.example.gitusers.db.entity.Followings
import com.example.gitusers.utilities.AdapterUtils
import com.example.gitusers.utilities.ListItem
import java.util.LinkedList

class MainModel{

    var mFollowings: List<Followings> = LinkedList()
        set(value) {
            field = value
            fillFollowings()
            updateList()
        }

    var showList: List<ListItem> = LinkedList()
    var list: List<User> = LinkedList()
        get() = field
        set(value) {
            field = value
            fillFollowings()
            updateList()
        }
    var isFilter: Boolean = false
        set(value) {
            field = value
            updateList()
        }

    private fun updateList() {
        showList = AdapterUtils().adaptData(list, isFilter)
    }

    private fun fillFollowings(){
        for (i in list.indices){
            var find = false
            for (j in mFollowings.indices)
                if(list[i].id!!.equals(mFollowings[j].id))
                    find = true
            list[i].follow = find
        }
//        list.forEach { it.follow = mFollowings.contains(Followings(null, it.id, true)) }
    }

    fun isFollow(mUser: User): Boolean {
        var find = false
        for (follow in mFollowings)
            if(follow.id == mUser.id) {
                find = true
                break
            }
        return find
    }


}
