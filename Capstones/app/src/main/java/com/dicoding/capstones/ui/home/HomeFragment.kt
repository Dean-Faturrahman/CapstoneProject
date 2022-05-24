package com.dicoding.capstones.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.capstones.R
import com.dicoding.capstones.adapter.itemhome.ListItemAdapter
import com.dicoding.capstones.adapter.itemhome.ListUserReviewAdapter
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.data.UserLoginModel
import com.dicoding.capstones.data.UserReview
import com.dicoding.capstones.databinding.FragmentHomeBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.ui.login.LoginActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showRecyclerList()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = PrefHelper(requireActivity());

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.getHomeData(sharedPref.getString(Const.PREF_USEREMAIL))

        homeViewModel.home.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it[0].userPhoto)
                .into(binding.imageUser)
            binding.tvUserName.text = it[0].userName
            sharedPref.put(Const.PREF_USERID, it[0].userId)
            binding.btnRole.text = it[0].userRole
            if (it[0].userRole == "Teacher") {
                binding.btnUpgrade.isVisible = false
            }
        }
    }

    private val listItemHome: ArrayList<ItemHome>
        get() {
            val dataName = resources.getStringArray(R.array.data_item_name)
            val dataPhoto = resources.obtainTypedArray(R.array.data_item_photo)
            val listItem = ArrayList<ItemHome>()
            for (i in dataName.indices) {
                val itemHome = ItemHome(dataPhoto.getResourceId(i, -1), dataName[i])
                listItem.add(itemHome)
            }
            return listItem
        }

    private val listItemReview: ArrayList<UserReview>
        get() {
            val dataName = resources.getStringArray(R.array.data_user_name)
            val dataPhoto = resources.obtainTypedArray(R.array.data_user_photo)
            val dataReview = resources.getStringArray(R.array.data_user_review)
            val listItem = ArrayList<UserReview>()
            for (i in dataName.indices) {
                val itemReview = UserReview(dataPhoto.getResourceId(i, -1), dataName[i], dataReview[i])
                listItem.add(itemReview)
            }
            return listItem
        }

    private fun showRecyclerList() {
        val listItemHomeAdapter = ListItemAdapter(listItemHome)
        binding.rvItem.adapter = listItemHomeAdapter

        val listItemReviewAdapter = ListUserReviewAdapter(listItemReview)
        binding.rvReview.adapter = listItemReviewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}