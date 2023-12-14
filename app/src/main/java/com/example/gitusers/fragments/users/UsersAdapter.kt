package com.example.gitusers.fragments.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitusers.databinding.HeaderItemRowBinding
import com.example.gitusers.databinding.UserItemRowBinding
import com.example.gitusers.utilities.ListItem
import java.util.LinkedList

class UsersAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var callbackListener : UsersCallback
    private var dataFilteredBy = LinkedList<ListItem>()


    fun setData(data:List<ListItem>){
        this.dataFilteredBy = LinkedList(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if(viewType == ListItem.ITEM_USER){
            UsersViewHolder(UserItemRowBinding.inflate(layoutInflater, parent, false))
        } else {
            HeaderViewHolder(HeaderItemRowBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return dataFilteredBy.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataFilteredBy[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UsersViewHolder) {
            val user = dataFilteredBy[position].user
            Glide.with(holder.itemBinder.image).asBitmap().load(user.avatarUrl).into(holder.itemBinder.image)
            holder.itemBinder.login.text = user.login
            holder.itemBinder.elevtion.isSelected = user.follow
            holder.itemView.setOnClickListener {
                callbackListener.onUserClick(user)
            }
            holder.itemBinder.elevtion.setOnClickListener {
                callbackListener.onUserFollowChange(user)
                holder.itemBinder.elevtion.isSelected = !holder.itemBinder.elevtion.isSelected
            }
        } else if(holder is HeaderViewHolder) {
            holder.itemBinder.letter.text = dataFilteredBy[position].letter
            holder.itemView.setOnClickListener {  }
        }
    }


    class UsersViewHolder(var itemBinder: UserItemRowBinding) : RecyclerView.ViewHolder(itemBinder.root)

    class HeaderViewHolder(var itemBinder: HeaderItemRowBinding) : RecyclerView.ViewHolder(itemBinder.root)
}