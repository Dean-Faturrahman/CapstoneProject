package com.dicoding.capstones.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstones.R
import com.dicoding.capstones.adapter.ListItemAdapter
import com.dicoding.capstones.adapter.ListItemSubjectAdapter
import com.dicoding.capstones.adapter.RatingAdapter
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.databinding.FragmentHomeBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.network.RatingDataItem
import com.dicoding.capstones.ui.classlist.ClassListActivity
import com.dicoding.capstones.ui.upgrade.FormUpgradeActivity
import com.google.android.material.bottomsheet.BottomSheetDialog



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showRecyclerList()
        action()
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = PrefHelper(requireActivity())

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.getHomeData(sharedPref.getString(Const.PREF_USEREMAIL))
        homeViewModel.getRatingData()

        homeViewModel.home.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it[0].userPhoto)
                .into(binding.imageUser)
            binding.tvUserName.text = getString(R.string.greetings) + it[0].userName
            sharedPref.put(Const.PREF_USERID, it[0].userId)
            binding.btnRole.text = it[0].userRole
            if (it[0].userRole == "Teacher") {
                binding.btnUpgrade.isVisible = false
            }
        }

        homeViewModel.rating.observe(viewLifecycleOwner){
            setRating(it)
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_subject, null)
        val rvItemDialog = view.findViewById<RecyclerView>(R.id.rv_item_dialog)

        val layoutManager = LinearLayoutManager(requireContext())
        rvItemDialog.layoutManager = layoutManager

        val listItemSubjectAdapter = ListItemSubjectAdapter(listItemSubject)
        rvItemDialog.adapter = listItemSubjectAdapter

        rvItemDialog.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

//        listItemHomeAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ItemHome?) {
//
//            }
//        })

        dialog.setContentView(view)
        dialog.show()
    }

    private fun action() {
        binding.btnUpgrade.setOnClickListener {
            val toUpgradeUser = Intent(requireContext(), FormUpgradeActivity::class.java)
            startActivity(toUpgradeUser)
        }
    }

    private val listItemHome: ArrayList<ItemHome>
        get() {
            val dataName = resources.getStringArray(R.array.data_item_name)
            val dataPhoto = resources.obtainTypedArray(R.array.data_item_photo)
            val listItem = ArrayList<ItemHome>()
            for (i in 1..7) {
                val itemHome = ItemHome(dataPhoto.getResourceId(i, -1), dataName[i])
                listItem.add(itemHome)
            }
            val lastItem = ItemHome(dataPhoto.getResourceId(0, -1), dataName[0])
            listItem.add(lastItem)
            return listItem
        }

    private val listItemSubject: ArrayList<ItemSubject>
        get() {
            val dataName = resources.getStringArray(R.array.data_item_name)
            val dataPhoto = resources.obtainTypedArray(R.array.data_item_photo)
            val listItem = ArrayList<ItemSubject>()
            for (i in 1 until dataName.size) {
                val itemHome = ItemSubject(dataPhoto.getResourceId(i, -1), dataName[i])
                listItem.add(itemHome)
            }
            return listItem
        }

//    private val listItemReview: ArrayList<UserReview>
//        get() {
//            val dataName = resources.getStringArray(R.array.data_user_name)
//            val dataPhoto = resources.obtainTypedArray(R.array.data_user_photo)
//            val dataReview = resources.getStringArray(R.array.data_user_review)
//            val listItem = ArrayList<UserReview>()
//            for (i in dataName.indices) {
//                val itemReview =
//                    UserReview(dataPhoto.getResourceId(i, -1), dataName[i], dataReview[i])
//                listItem.add(itemReview)
//            }
//            return listItem
//        }

    private fun setRating(rating : List<RatingDataItem>){
        val listRating =ArrayList<RatingDataItem>()

        for (i in rating){
            val ratingUser = RatingDataItem(
                i.userPhoto,
                i.userName,
                i.orderRating,
                i.orderRatingDesc
            )
            listRating.add(ratingUser)
        }

        val adapter = RatingAdapter(listRating)
        binding.rvReview.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        binding.rvReview.layoutManager = layoutManager
    }

    private fun showRecyclerList() {
        val listItemHomeAdapter = ListItemAdapter(listItemHome)
        binding.rvItem.adapter = listItemHomeAdapter
        listItemHomeAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemHome?) {
                if (data?.name == "Lainnya") {
                    showBottomSheet()
                } else {
                    toClass(data?.name)
                }
            }
        })

//        val listItemReviewAdapter = ListUserReviewAdapter(listRating)
//        binding.rvReview.adapter = listItemReviewAdapter
    }

    private fun toClass(data: String?) {
        val toClass = Intent(requireActivity(), ClassListActivity::class.java)
        toClass.putExtra(ClassListActivity.EXTRA_SUBJECT, data)
        startActivity(toClass)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}