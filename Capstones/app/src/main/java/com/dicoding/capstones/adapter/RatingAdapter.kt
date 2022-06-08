package com.dicoding.capstones.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstones.databinding.ItemReviewBinding
import com.dicoding.capstones.network.RatingDataItem

class RatingAdapter(private val listItemRating: ArrayList<RatingDataItem>) : RecyclerView.Adapter<RatingAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, username, order_rating, order_ratingdesc) = listItemRating[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.binding.imgItemPhoto)
        holder.binding.nameUser.text = username
//        holder.binding.rating.isEnabled = false
        holder.binding.rating.rating = order_rating.toFloat()
        holder.binding.reviewUser.text = order_ratingdesc
    }

    override fun getItemCount(): Int = listItemRating.size
}