package com.dicoding.capstones.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.capstones.R
import com.dicoding.capstones.adapter.itemhome.ListItemAdapter
import com.dicoding.capstones.adapter.itemhome.ListUserReviewAdapter
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.data.UserReview
import com.dicoding.capstones.databinding.FragmentHomeBinding

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        showRecyclerList()
        return root
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