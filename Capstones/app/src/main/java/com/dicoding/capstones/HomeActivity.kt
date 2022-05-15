package com.dicoding.capstones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.adapter.itemhome.ListItemAdapter
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()
    }

    private val listHeroes: ArrayList<ItemHome>
    get() {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listItem = ArrayList<ItemHome>()
        for (i in dataName.indices) {
            val hero = ItemHome(dataPhoto.getResourceId(i, -1), dataName[i])
            listItem.add(hero)
        }
        return listItem
    }

    private fun showRecyclerList() {
        val listHeroAdapter = ListItemAdapter(listHeroes)
        binding.rvItem.adapter = listHeroAdapter
    }
}