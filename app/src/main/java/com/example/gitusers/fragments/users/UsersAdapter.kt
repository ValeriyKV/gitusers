package com.example.gitusers.fragments.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitApp.data.User
import com.example.gitusers.R
import com.example.gitusers.UsersCallback
import com.example.gitusers.utilities.ListItem
import java.util.LinkedList

class UsersAdapter(private var layoutInflater : LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var callbackListener : UsersCallback
    private var dataFilteredBy = LinkedList<ListItem>()


    fun setData(data:List<ListItem>){
        this.dataFilteredBy = LinkedList(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ListItem.ITEM_USER){
            UsersViewHolder(layoutInflater.inflate(R.layout.user_item_row, parent, false))
        } else {
            HeaderViewHolder(layoutInflater.inflate(R.layout.header_item_row, parent, false))
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
            holder.bind(user)
            holder.itemView.setOnClickListener {
                callbackListener.onUserClick(user)
            }
            holder.selector.setOnClickListener {
                callbackListener.onUserFollowChange(user)
                holder.selector.isSelected = !holder.selector.isSelected
            }
        } else if(holder is HeaderViewHolder) {
            holder.bind(dataFilteredBy[position].letter)
            holder.itemView.setOnClickListener {  }
        }
    }


    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imageView:ImageView = itemView.findViewById(R.id.image)
        private var login:TextView = itemView.findViewById(R.id.login)
        var selector:ImageView = itemView.findViewById(R.id.elevtion)

        fun bind(user: User) {
            Glide.with(imageView).asBitmap().load(user.avatarUrl).into(imageView)
            login.text = user.login
            selector.isSelected = user.follow
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var letter:TextView = itemView.findViewById(R.id.letter)

        fun bind(letter : String){
            this.letter.text = letter
        }
    }
}