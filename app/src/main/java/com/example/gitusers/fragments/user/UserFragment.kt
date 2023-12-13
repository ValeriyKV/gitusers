package com.example.gitusers.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.gitApp.data.User
import com.example.gitusers.R
import com.example.gitusers.db.Database
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    
    private lateinit var mImageView : ImageView
    private lateinit var mName : TextView
    private lateinit var mLocation : TextView
    private lateinit var mNickName : TextView
    private lateinit var mToolbar: Toolbar
    
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mImageView = view.findViewById(R.id.image)
        mName = view.findViewById(R.id.name)
        mNickName = view.findViewById(R.id.nickname)
        mLocation = view.findViewById(R.id.place)
        mToolbar = view.findViewById(R.id.toolbar)
        mToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        var arguments = arguments
        var user:User? = null
        if(arguments != null && arguments.containsKey("user")){
            user = arguments.getParcelable<User>("user")
            if(user != null && !user.isEmpty())
                fillData(user)
        } else {
            Navigation.findNavController(view).navigateUp()
        }
        viewModel = ViewModelProvider(this, UserViewModel.Factory(Database.createDatabase(), user!!))[UserViewModel::class.java]
        mToolbar.setOnMenuItemClickListener{
            viewModel.run {
                viewModel.updateFollowings()
            }
            return@setOnMenuItemClickListener true
        }
        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            fillData(it)
        }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(view).navigateUp()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun fillData(user: User) {
        Glide.with(this).asBitmap().load(user.avatarUrl).into(mImageView)
        mName.text = user.name
        mLocation.text = user.location
        mNickName.text = user.location
        if (user.follow){
            mToolbar.menu.findItem(R.id.following).setIcon(R.drawable.baseline_star_24)
        } else {
            mToolbar.menu.findItem(R.id.following).setIcon(R.drawable.baseline_star_border_24)
        }
    }
}