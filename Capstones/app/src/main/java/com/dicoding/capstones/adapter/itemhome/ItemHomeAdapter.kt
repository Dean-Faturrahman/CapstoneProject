package com.dicoding.capstones.adapter.itemhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.databinding.ItemBinding

class ListItemAdapter(private val listItemHome: ArrayList<ItemSubject>) : RecyclerView.Adapter<ListItemAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, name) = listItemHome[position]
        holder.binding.imgItem.setImageResource(photo)
        holder.binding.tvItemName.text = name
    }

    override fun getItemCount(): Int = listItemHome.size
}