package com.dicoding.capstones.ui.classlist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.adapter.itemhome.ClassListAdapter
import com.dicoding.capstones.data.ChatListModel
import com.dicoding.capstones.databinding.ActivityClassListBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.network.ClassList
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ClassListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassListBinding
    private val classListViewModel by viewModels<ClassListViewModel>()
    private lateinit var sharedPref: PrefHelper
    private lateinit var db: FirebaseDatabase
    private val listClass = ArrayList<ClassList>()
    private var subject: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarConfig()

        val subject = intent.getStringExtra(EXTRA_SUBJECT)
        getClassList(subject)

        sharedPref = PrefHelper(this)
        db = Firebase.database

        classListViewModel.orderlist.observe(this@ClassListActivity) { dataItem ->
            classListViewModel.getDataStudent(sharedPref.getString(Const.PREF_USERID))
            classListViewModel.dataStudent.observe(this@ClassListActivity) { dataStudent ->
                val student = ChatListModel(
                    id = dataStudent.first().userId,
                    photo = dataStudent.first().userPhoto,
                    name = dataStudent.first().userName
                )
                db.reference.child("chat").child(dataItem.orderId).child("student")
                    .setValue(student)
            }

            classListViewModel.getDataTeacher(dataItem.teacherId)
            classListViewModel.dataTeacher.observe(this@ClassListActivity) { dataTeacher ->
                val teacher = ChatListModel(
                    id = dataTeacher.first().userId,
                    photo = dataTeacher.first().userPhoto,
                    name = dataTeacher.first().userName,
                    subject = subject
                )
                db.reference.child("chat").child(dataItem.orderId).child("teacher")
                    .setValue(teacher)
                finish()
            }
            db.reference.child("chat").child(dataItem.orderId).child("status")
                .setValue(dataItem.orderStatus)
        }
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

        listClassAdapter.setOnItemClickCallback(object : ClassListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ClassList?) {
//                toChat()
//                Log.e("Check Order", data?.classId.toString())
//                Log.e("Check Order", sharedPref.getString(Const.PREF_USERID).toString())
                classListViewModel.createOrder(
                    data?.classId,
                    sharedPref.getString(Const.PREF_USERID)
                )
                subject = data?.subjectName.toString()
            }
        })


    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvClass.layoutManager = layoutManager
        binding.rvClass.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        );
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toChat() {
//        finish()
//        val intent = Intent(this, ChatFragment::class.java)
//        startActivity(intent)

//        val myFragment = ChatFragment();
//        supportFragmentManager.beginTransaction().add(, myFragment)
//

//        navController.navigateUp()
//        navController.navigate(R.id.nav_chat)
    }

    companion object {
        const val EXTRA_SUBJECT = "extra_subject"
    }
}