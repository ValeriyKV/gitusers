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
import com.example.gitusers.databinding.FragmentUserBinding
import com.example.gitusers.db.Database
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
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
        binding.toolbar.setOnMenuItemClickListener{
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
        Glide.with(this).asBitmap().load(user.avatarUrl).into(binding.image)
        binding.name.text = user.name
        binding.place.text = user.location
        binding.nickname.text = user.login
        if (user.follow){
            binding.toolbar.menu.findItem(R.id.following).setIcon(R.drawable.baseline_star_24)
        } else {
            binding.toolbar.menu.findItem(R.id.following).setIcon(R.drawable.baseline_star_border_24)
        }
    }
}