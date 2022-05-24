package com.dicoding.capstones.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.capstones.databinding.FragmentProfileBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.ui.login.LoginActivity
import java.util.*

class   ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPref: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPref = PrefHelper(requireContext())


        profileViewModel.getProfileData(sharedPref.getString(Const.PREF_USERID))

        profileViewModel.profile.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it[0].userPhoto)
                .into(binding.imgItemPhoto)
            binding.tvNama.text = it[0].userName
            binding.tvGender.text = it[0].userGender
            binding.tvEmail.text = it[0].userEmail
            binding.tvNohp.text = it[0].userPhone
            binding.tvPendidikan.text = it[0].userRole
//            var yearNow: Int = Calendar.getInstance().get(Calendar.YEAR)
//            var dateAge = it[0].userDOB
//            var yearAge = dateAge.subSequence(0,4)
//
//            var age: Int = yearNow - yearAge
//
//            binding.tvAge.text = age.toString()
            sharedPref.put(Const.PREF_USERID, it[0].userId)
            binding.btnRole.text = it[0].userRole
        }

        logout()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        binding.tvLogout.setOnClickListener {
            sharedPref.clear()
            Toast.makeText(requireContext(), "Logout Success", Toast.LENGTH_SHORT).show()
            val toLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(toLogin)
            activity?.finish()
        }
    }
}