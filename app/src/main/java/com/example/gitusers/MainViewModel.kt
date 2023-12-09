package com.example.gitusers

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.LinkedList
import java.util.concurrent.Executors

class MainViewModel(database: Database) : ViewModel(){

    private var mModel : MainModel = MainModel()
    private var mUser : User = User()
    private var mFollowingsDoa:FollowingsDoa

    var mutableStateFlow = MutableStateFlow(mModel.showList)
    val modelFlow : StateFlow<List<ListItem>> = mutableStateFlow.stateIn(viewModelScope, SharingStarted.Eagerly, mModel.showList)

    var mutableStateFlowUsers = MutableStateFlow(mModel.list)
    val modelFlowUsers : StateFlow<List<User>> = mutableStateFlowUsers.stateIn(viewModelScope, SharingStarted.Eagerly, mModel.list)

    var mutableStateFlowUser = MutableStateFlow(mUser)
    val modelFlowUser : StateFlow<User> = mutableStateFlowUser.stateIn(viewModelScope, SharingStarted.Eagerly, mUser)


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
                viewModelScope.launch {
                    mutableStateFlowUsers.emit(mModel.list)
                }
            }
        }
    }

    fun getFullInformation(user: User) {
        val userAPI = APIClient.getClient().create(UserAPI::class.java)
        val userCall = user.url?.let { userAPI.getUserInfo(it) }
        val function = object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        mUser = body
                        mUser.follow = mModel.isFollow(mUser)
                        viewModelScope.launch {
                            mutableStateFlowUser.emit(mUser)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        }
        userCall?.enqueue(function)
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