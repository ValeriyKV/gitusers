package com.example.gitusers.fragments.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gitApp.api.APIClient
import com.example.gitApp.api.user.UserAPI
import com.example.gitApp.data.User
import com.example.gitusers.db.Database
import com.example.gitusers.db.dao.FollowingsDoa
import com.example.gitusers.db.entity.Followings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class UserViewModel(database: Database, user : User) : ViewModel() {

    var userModel : UserModel = UserModel()
    var mutableStateFlowUser = MutableStateFlow(userModel.user)
    val modelFlowUser : StateFlow<User> = mutableStateFlowUser.stateIn(viewModelScope, SharingStarted.Eagerly, userModel.user)
    private var mFollowingsDoa: FollowingsDoa

    init {
        mFollowingsDoa = database.followingsDoa()
        userModel.user = user
        viewModelScope.launch {
            user.id?.let { mFollowingsDoa.getFollowingByUserId(userId = it).collect{
                userModel.updateFollowing(if(it==null) Followings(null, user.id,  follow = false) else it)
                mutableStateFlowUser.emit(userModel.user)
            } }
        }
        getFullInformation(user)
    }

    fun getFullInformation(user: User) {
        val userAPI = APIClient.getClient().create(UserAPI::class.java)
        val userCall = user.url?.let { userAPI.getUserInfo(it) }
        val function = object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        userModel.user  =body
                        viewModelScope.launch {
                            mutableStateFlowUser.emit(userModel.user)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        }
        userCall?.enqueue(function)
    }

    fun updateFollowings() {
        Executors.newSingleThreadExecutor().submit {
            var userToUpdate = userModel.user
            if(userToUpdate.follow){
                mFollowingsDoa.delete(userToUpdate.id!!)
            } else {
                mFollowingsDoa.insertAll(Followings(null, userToUpdate.id, true))
            }
        }
    }

    class Factory(
        var database: Database,
        var user: User
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return try {
                modelClass.getConstructor(
                    Database::class.java,
                    User::class.java
                ).newInstance(database, user)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

}