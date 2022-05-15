package com.dicoding.capstones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.capstones.adapter.itemhome.ListItemAdapter
import com.dicoding.capstones.adapter.itemhome.ListUserReviewAdapter
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.data.UserReview
import com.dicoding.capstones.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()
    }

    private val listItemHome: ArrayList<ItemSubject>
    get() {
        val dataName = resources.getStringArray(R.array.data_item_name)
        val dataPhoto = resources.obtainTypedArray(R.array.data_item_photo)
        val listItem = ArrayList<ItemSubject>()
        for (i in dataName.indices) {
            val itemHome = ItemSubject(dataPhoto.getResourceId(i, -1), dataName[i])
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
}