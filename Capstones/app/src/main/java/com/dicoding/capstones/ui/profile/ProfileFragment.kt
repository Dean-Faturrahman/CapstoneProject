package com.dicoding.capstones.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dicoding.capstones.R
import com.dicoding.capstones.databinding.FragmentProfileBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.ui.editeprofile.EditProfileActivity
import com.dicoding.capstones.ui.login.LoginActivity
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: PrefHelper
    private val profileViewModel by viewModels<ProfileViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPref = PrefHelper(requireContext())

        var dateAge: String? = null
        var photo: String? = null

        profileViewModel.getProfileData(sharedPref.getString(Const.PREF_USERID))
        profileViewModel.profile.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it[0].userPhoto)
                .into(binding.imgItemPhoto)
            photo = it[0].userPhoto
            binding.tvNama.text = it[0].userName
            binding.tvGender.text = it[0].userGender
            binding.tvEmail.text = it[0].userEmail
            binding.tvNohp.text = it[0].userPhone
            binding.tvPendidikan.text = it[0].userRole
            val yearNow: Int = Calendar.getInstance().get(Calendar.YEAR)
            dateAge = it[0].userDOB
            val yearAge = dateAge?.subSequence(0, 4).toString().toInt()
            var age: Int = yearNow - yearAge
            if (yearNow < yearAge) {
                age--
            }
            binding.tvAge.text = age.toString() + getString(R.string.yearsOld)
            sharedPref.put(Const.PREF_USERID, it[0].userId)
            binding.btnRole.text = it[0].userRole
        }

        binding.btnEdit.setOnClickListener {
            val name = binding.tvNama.text
            val hp = binding.tvNohp.text
            val gender = binding.tvGender.text
            val profile = photo
            val date = dateAge
            val toEditProfile = Intent(requireContext(), EditProfileActivity::class.java)

            toEditProfile.putExtra(EditProfileActivity.EXTRA_NAME, name)
            toEditProfile.putExtra(EditProfileActivity.EXTRA_NOMOR, hp)
            toEditProfile.putExtra(EditProfileActivity.EXTRA_GENDER, gender)
            toEditProfile.putExtra(EditProfileActivity.EXTRA_PHOTO, profile)
            toEditProfile.putExtra(EditProfileActivity.EXTRA_DATE, date)

            startActivity(toEditProfile)
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

    override fun onResume() {
        super.onResume()
        profileViewModel.getProfileData(sharedPref.getString(Const.PREF_USERID))
    }
}