package com.dicoding.capstones.adapter.itemhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstones.data.ItemHome
import com.dicoding.capstones.databinding.ItemListClassBinding
import com.dicoding.capstones.network.ClassList

class ClassListAdapter(private val listClass: ArrayList<ClassList>) : RecyclerView.Adapter<ClassListAdapter.ListViewHolder>(){
    class ListViewHolder(var binding: ItemListClassBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, name, gender, photo, subject) = listClass[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvTeacherName.text = name
        holder.binding.tvTeacherGender.text = gender
        holder.binding.tvTeacherDesc.text = subject
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemHome?)
    }

    override fun getItemCount(): Int = listClass.size
}