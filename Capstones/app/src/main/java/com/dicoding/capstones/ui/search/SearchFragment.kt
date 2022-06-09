package com.dicoding.capstones.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.R
import com.dicoding.capstones.adapter.SearchListAdapter
import com.dicoding.capstones.data.ChatListModel
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.databinding.FragmentSearchBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.network.ClassList
import com.dicoding.capstones.network.SearchDataItem
import com.dicoding.capstones.ui.chat.ChatFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var sharedPref: PrefHelper
    private var subject: String? = null
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = PrefHelper(requireActivity())
        db = Firebase.database

        searchService()
        getPredictions(sharedPref.getString(Const.PREF_USERID))
        searchViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        errorObserver()
        getSuggestions()

        Log.e("test value", searchViewModel.orderlist.value.toString())

        searchViewModel.orderlist.observe(viewLifecycleOwner) { dataItem ->
            Log.e("test observe", "Test")
            Log.e("test value inside observe", searchViewModel.orderlist.value.toString())
            if (subject != null) {
                searchViewModel.getDataStudent(sharedPref.getString(Const.PREF_USERID))
                searchViewModel.dataStudent.observe(viewLifecycleOwner) { dataStudent ->
                    val student = ChatListModel(
                        id = dataStudent.first().userId,
                        photo = dataStudent.first().userPhoto,
                        name = dataStudent.first().userName
                    )
                    db.reference.child("chat").child(dataItem.orderId).child("student")
                        .setValue(student)
                }

                searchViewModel.getDataTeacher(dataItem.teacherId)
                searchViewModel.dataTeacher.observe(viewLifecycleOwner) { dataTeacher ->
                    val teacher = ChatListModel(
                        id = dataTeacher.first().userId,
                        photo = dataTeacher.first().userPhoto,
                        name = dataTeacher.first().userName,
                        subject = subject
                    )
                    db.reference.child("chat").child(dataItem.orderId).child("teacher")
                        .setValue(teacher)
                    findNavController().navigateUp()
                    findNavController().navigate(R.id.nav_chat)
                }
                db.reference.child("chat").child(dataItem.orderId).child("status")
                    .setValue(dataItem.orderStatus)
            }
        }
    }

    private fun searchService() {
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewModel.listUser.observe(viewLifecycleOwner) { users ->
            setUser(users)
            binding.searchView.clearFocus()
        }

        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        binding.searchView.queryHint = resources.getString(R.string.search_hint)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchView.clearFocus()
                searchUserData(p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun searchUserData(q: String) {
        searchViewModel.searchDataUser(q)
        searchViewModel.listUser.observe(this) { users -> setUser(users) }
    }

    private fun getPredictions(userId: String?) {
        searchViewModel.getPredictions(userId)
    }

    private fun getSuggestions() {
        searchViewModel.suggestions.observe(viewLifecycleOwner) {
            setUser(it)
        }
    }

    private fun errorObserver() {
        searchViewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUser(users: List<SearchDataItem>) {
        val listUser = ArrayList<SearchDataItem>()
        listUser.clear()
        for (user in users) {
            val us = SearchDataItem(
                user.classId,
                user.userName,
                user.userGender,
                user.userPhoto,
                user.subjectName
            )
            listUser.add(us)
        }
        val adapter = SearchListAdapter(listUser)
        binding.rvListGuru.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        binding.rvListGuru.layoutManager = layoutManager

        binding.rvListGuru.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        adapter.setOnItemClickCallback(object : SearchListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: SearchDataItem?) {
                db = Firebase.database
                searchViewModel.createOrder(
                    data?.classId,
                    sharedPref.getString(Const.PREF_USERID)
                )
                subject = data?.subjectName.toString()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}