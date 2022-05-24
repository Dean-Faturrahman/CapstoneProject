package com.dicoding.capstones.ui.classlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.adapter.itemhome.ClassListAdapter
import com.dicoding.capstones.adapter.itemhome.ListItemAdapter
import com.dicoding.capstones.adapter.itemhome.ListUserReviewAdapter
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.databinding.ActivityClassListBinding
import com.dicoding.capstones.network.ClassList
import com.dicoding.capstones.ui.login.LoginViewModel

class ClassListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassListBinding
    private val classListViewModel by viewModels<ClassListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarConfig()

        val subject = intent.getStringExtra(EXTRA_SUBJECT)
        getClassList(subject)
    }

    private fun actionBarConfig() {
        supportActionBar?.title = "Pengajar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun getClassList(q: String?) {
        classListViewModel.getClassList(q)
        classListViewModel.classlist.observe(this) {
            setClassData(it)
        }
        classListViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setClassData(classes: List<ClassList>) {
        val listClass = ArrayList<ClassList>()
        listClass.clear()
        for (cls in classes) {
            val cl = ClassList(
                cls.classId,
                cls.userName,
                cls.userGender,
                cls.userPhoto,
                cls.subjectName,
            )
            listClass.add(cl)
        }

        showRecyclerList()
        val listClassAdapter = ClassListAdapter(listClass)
        binding.rvClass.adapter = listClassAdapter
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvClass.layoutManager = layoutManager
        binding.rvClass.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


//        listClassAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ItemHome?) {
//                toClass(data)
//            }
//        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_SUBJECT = "extra_subject"
    }
}