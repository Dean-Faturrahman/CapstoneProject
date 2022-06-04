package com.dicoding.capstones.adapter.itemhome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capstones.data.ChatListModel
import com.dicoding.capstones.databinding.ItemChatBinding

class ChatAdapter(private var listChat: ArrayList<ChatListModel>) : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, _, photo, name, subject, lastMessages, unseenMessages) = listChat[position]
        Glide.with(holder.itemView)
            .load(photo)
            .into(holder.binding.imgItemPhoto)
        holder.binding.nameUser.text = name
        holder.binding.tvSubject.text = subject
        holder.binding.tvMessageReceived.text = lastMessages

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listChat[holder.adapterPosition])
        }
    }

    fun updateData(listChat: ArrayList<ChatListModel>) {
        this.listChat = listChat
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listChat.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ChatListModel)
    }
}