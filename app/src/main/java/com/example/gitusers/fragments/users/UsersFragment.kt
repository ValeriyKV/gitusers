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
import com.example.gitusers.MainViewModel
import com.example.gitusers.R
import com.example.gitusers.UsersCallback
import com.example.gitusers.db.Database
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersFragment : Fragment(), UsersCallback, Toolbar.OnMenuItemClickListener{

    private lateinit var viewModel: MainViewModel

    private lateinit var usersAdapter : UsersAdapter
    private lateinit var menuItem: ActionMenuItemView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, MainViewModel.Factory(Database.createDatabase()))[MainViewModel::class.java]
        val recyclerView :RecyclerView = view.findViewById(R.id.listView)
        usersAdapter = UsersAdapter(LayoutInflater.from(view.context))
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = usersAdapter
        usersAdapter.callbackListener = this@UsersFragment
        view.findViewById<Toolbar>(R.id.toolbar).setOnMenuItemClickListener(this)
        menuItem = view.findViewById(R.id.filter)
        bindObservers()
    }

    private fun bindObservers() {
        val lifecycleScope = lifecycle.coroutineScope
        lifecycleScope.launch {
            viewModel.modelFlow.collectLatest {
                usersAdapter.setData(it)
            }
        }
        lifecycleScope.launch{
            viewModel.modelFlowUser.collect {
                if (!it.isEmpty()) {
                    val currentDestination = findNavController().currentDestination
                    val action =
                        currentDestination?.getAction(R.id.action_usersFragment_to_userFragment)
                    if (action != null) {
                        val bundle = bundleOf("user" to it)
                        findNavController().navigate(
                            R.id.action_usersFragment_to_userFragment,
                            bundle
                        )
                        viewModel.mutableStateFlowUser.update { User() }
                    }
                }
            }
        }
    }

    override fun onUserClick(user: User) {
        viewModel.getFullInformation(user)
    }

    override fun onUserFollowChange(user: User) {
        viewModel.updateFollowings(user)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId?.equals(R.id.filter) == true){
            viewModel.changeFilter()
        }
        return true
    }
}