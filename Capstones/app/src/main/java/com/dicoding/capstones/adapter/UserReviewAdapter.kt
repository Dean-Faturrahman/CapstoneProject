package com.dicoding.capstones.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstones.data.UserReview
import com.dicoding.capstones.databinding.ItemReviewBinding

class ListUserReviewAdapter(private val listUserReview: ArrayList<UserReview>) : RecyclerView.Adapter<ListUserReviewAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, name, review) = listUserReview[position]
        holder.binding.imgItemPhoto.setImageResource(photo)
        holder.binding.nameUser.text = name
        holder.binding.reviewUser.text = review
    }

    override fun getItemCount(): Int = listUserReview.size
}