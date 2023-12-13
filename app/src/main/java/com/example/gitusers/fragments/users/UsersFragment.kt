package com.example.gitusers.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitApp.data.User
import com.example.gitusers.R
import com.example.gitusers.UsersCallback
import com.example.gitusers.databinding.FragmentUsersBinding
import com.example.gitusers.db.Database
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UsersFragment : Fragment(), UsersCallback{

    private lateinit var viewModel: UsersViewModel

    private lateinit var usersAdapter : UsersAdapter

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, UsersViewModel.Factory(Database.createDatabase()))[UsersViewModel::class.java]
        usersAdapter = UsersAdapter()
        usersAdapter.callbackListener = this@UsersFragment
        binding.listView.run {
            layoutManager = LinearLayoutManager(view.context)
            adapter = usersAdapter
        }
        binding.toolbar.menu.findItem(R.id.filter).setOnMenuItemClickListener {
            viewModel.changeFilter()
            return@setOnMenuItemClickListener true
        }
        bindObservers()
    }

    private fun bindObservers() {
        val lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            viewModel.modelFlow.collectLatest {
                usersAdapter.setData(it)
            }
        }
    }

    override fun onUserClick(user: User) {
        val currentDestination = findNavController().currentDestination
        val action =
            currentDestination?.getAction(R.id.action_usersFragment_to_userFragment)
        if (action != null) {
            val bundle = bundleOf("user" to user)
            findNavController().navigate(
                R.id.action_usersFragment_to_userFragment,
                bundle
            )
        }
    }

    override fun onUserFollowChange(user: User) {
        viewModel.updateFollowings(user)
    }
}