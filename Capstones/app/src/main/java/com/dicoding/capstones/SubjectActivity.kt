package com.dicoding.capstones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.capstones.adapter.itemhome.ListItemSubjectAdapter
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.databinding.ActivitySubjectBinding

class SubjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()
    }

    private val listItemSubject: ArrayList<ItemSubject>
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

    private fun showRecyclerList() {
        val listItemSubject = ListItemSubjectAdapter(listItemSubject)
        binding.rvSubject.adapter = listItemSubject
    }
}