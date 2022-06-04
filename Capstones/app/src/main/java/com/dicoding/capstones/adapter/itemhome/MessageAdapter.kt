package com.dicoding.capstones.adapter.itemhome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstones.R
import com.dicoding.capstones.data.MessageModel
import com.dicoding.capstones.databinding.ItemMessageBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MessageAdapter(
    options: FirebaseRecyclerOptions<MessageModel>,
    private val currentId: String?
) : FirebaseRecyclerAdapter<MessageModel, MessageAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: MessageModel) {
        holder.bind(model)
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageModel) {
            if (item.id == currentId) {
                binding.tvMessageSender.text = item.text
                binding.tvMessageReceiver.visibility = View.GONE
            } else {
                binding.tvMessageReceiver.text = item.text
                binding.tvMessageSender.visibility = View.GONE
            }
        }
    }
}