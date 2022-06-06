package com.dicoding.capstones.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.R
import com.dicoding.capstones.adapter.SearchListAdapter
import com.dicoding.capstones.databinding.FragmentSearchBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.network.SearchDataItem

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var sharedPref: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = PrefHelper(requireActivity())

//        errorObserver()
        searchViewModel.isLoading.observe(requireActivity()) { showLoading(it)}
        searchService()
        getPredictions(sharedPref.getString(Const.PREF_USERID))
        getSuggestions()
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

    private fun searchUserData(q: String){
        searchViewModel.searchDataUser(q)
        searchViewModel.listUser.observe(this) { users -> setUser(users) }
    }

    private fun getPredictions(userId: String?) {
        searchViewModel.getPredictions(userId)
    }

    private fun getSuggestions() {
        searchViewModel.suggestions.observe(requireActivity()) {
            setUser(it)
        }
    }

    private fun errorObserver() {
        searchViewModel.errorMessage.observe(requireActivity()) {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}