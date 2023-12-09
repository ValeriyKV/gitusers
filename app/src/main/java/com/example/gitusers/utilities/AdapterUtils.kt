package com.example.gitusers.utilities

import com.example.gitApp.data.User
import java.util.LinkedList

class AdapterUtils {

    fun adaptData(list: List<User>, useFilter: Boolean): List<ListItem> {
        return if(useFilter) filterData(list) else fillData(list)
    }

    private fun filterData(list: List<User>): List<ListItem> {
        if(list.isEmpty()) return LinkedList()
        val res = LinkedList<ListItem>()
        val sortedList = list.sortedBy { it.login.toString().lowercase() }
        var letter : String = sortedList[0].login?.get(0)?.toString()?.uppercase() ?: ""
        res.add(ListItem(letter = letter, type = ListItem.ITEM_LITTER))
        for (user in sortedList){
            if(user.login?.startsWith(letter, true) == false){
                letter = user.login?.get(0)?.toString()?.uppercase() ?: ""
                res.add(ListItem(letter = letter, type = ListItem.ITEM_LITTER))
            }
            res.add(ListItem(user = user, type = ListItem.ITEM_USER))
        }
        return res
    }

    private fun fillData(list: List<User>): List<ListItem> {
        val res = LinkedList<ListItem>()
        list.forEach { res.add(ListItem(it, "", ListItem.ITEM_USER)) }
        return res
    }

}