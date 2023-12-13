package com.example.gitusers.fragments.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gitApp.api.APIClient
import com.example.gitApp.api.user.UserAPI
import com.example.gitApp.data.User
import com.example.gitusers.db.Database
import com.example.gitusers.db.dao.FollowingsDoa
import com.example.gitusers.db.entity.Followings
import com.example.gitusers.utilities.ListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList
import java.util.concurrent.Executors

class UsersViewModel(database: Database) : ViewModel() {

    private var mModel : UsersModel = UsersModel()
    private var mFollowingsDoa: FollowingsDoa

    var mutableStateFlow = MutableStateFlow(mModel.showList)
    val modelFlow : StateFlow<List<ListItem>> = mutableStateFlow.stateIn(viewModelScope, SharingStarted.Eagerly, mModel.showList)

    init {
        mFollowingsDoa = database.followingsDoa()
        val userAPI = APIClient.getClient().create(UserAPI::class.java)
        val usersCall = userAPI.getUsers()
        val function = object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if(!body.isNullOrEmpty()){
//                        mModel = MainModel()
                        mModel.list = body
                        viewModelScope.launch {
                            mutableStateFlow.emit(LinkedList(mModel.showList))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e(t.message, t.localizedMessage)
            }
        }
        usersCall.enqueue(function)
        viewModelScope.launch {
            mFollowingsDoa.getAll().collectLatest {
                mModel.mFollowings = it
                mModel.showList = LinkedList(mModel.showList)
            }
        }
    }

    fun changeFilter(){
        mModel.isFilter = !mModel.isFilter
        viewModelScope.launch {
            mutableStateFlow.emit(LinkedList(mModel.showList))
        }
    }

    fun updateFollowings(user: User) {
        Executors.newSingleThreadExecutor().submit {
            var userToUpdate = user
            mModel.list.forEach{ if (it.id == user.id) userToUpdate = it }
            if(userToUpdate.follow){
                mFollowingsDoa.delete(user.id!!)
            } else {
                mFollowingsDoa.insertAll(Followings(null, user.id, true))
            }
        }
    }

    class Factory(
        var database: Database
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return try {
                modelClass.getConstructor(
                    Database::class.java
                ).newInstance(database)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

}