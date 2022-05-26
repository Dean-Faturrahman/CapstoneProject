package com.dicoding.capstones.adapter.itemhome

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.capstones.ui.chat.StudentChatFragment
import com.dicoding.capstones.ui.chat.TeacherChatFragment

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = StudentChatFragment()
            1 -> fragment = TeacherChatFragment()
        }
        return fragment as Fragment
    }
}