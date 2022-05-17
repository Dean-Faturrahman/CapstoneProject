package com.dicoding.capstones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.nav.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nav()
    }

    private fun nav(){
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false
    }
}