package com.dicoding.capstones.adapter.itemhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstones.data.ItemSubject
import com.dicoding.capstones.databinding.ItemSubjectBinding

class ListItemSubjectAdapter(private val listItemSubject: ArrayList<ItemSubject>) : RecyclerView.Adapter<ListItemSubjectAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemSubjectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, name) = listItemSubject[position]
        holder.binding.imgItemPhoto.setImageResource(photo)
        holder.binding.nameSubject.text = name
    }

    override fun getItemCount(): Int = listItemSubject.size
}