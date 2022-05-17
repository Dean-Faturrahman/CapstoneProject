package com.dicoding.capstones

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dicoding.capstones.adapter.itemhome.ListItemAdapter
import com.dicoding.capstones.adapter.itemhome.ListUserReviewAdapter
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.data.UserReview
import com.dicoding.capstones.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.nav.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nav()
        showRecyclerList()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.search -> {
                val i = Intent(this, SearchActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }

    private fun nav(){
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false
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