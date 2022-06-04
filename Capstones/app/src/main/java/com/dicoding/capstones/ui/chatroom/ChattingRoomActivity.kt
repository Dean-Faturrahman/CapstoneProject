package com.dicoding.capstones.ui.chatroom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.adapter.itemhome.MessageAdapter
import com.dicoding.capstones.data.MessageModel
import com.dicoding.capstones.databinding.ActivityChattingRoomBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChattingRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChattingRoomBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getName = intent.getStringExtra(EXTRA_NAME)
        val getId = intent.getStringExtra(EXTRA_ID)
        val getIdUser = intent.getStringExtra(EXTRA_IDUSER)
        val getOrderId = intent.getStringExtra(EXTRA_ORDERID)

        Log.e("Check Order ID", getOrderId.toString())

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.nameUserChat.text = getName

        db = Firebase.database

        val messagesRef = db.reference.child("chat").child(getOrderId.toString()).child("messages")

        binding.sendButton.setOnClickListener {
            val msg = MessageModel(
                binding.messageEditText.text.toString(),
                getIdUser

            )
            messagesRef.push().setValue(msg) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, "Error : " + error.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
            }
            binding.messageEditText.setText("")
        }

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<MessageModel>()
            .setQuery(messagesRef, MessageModel::class.java)
            .build()
        adapter = MessageAdapter(options, getIdUser)
        binding.messageRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    companion object {
        const val EXTRA_ORDERID = "extra_id"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_IDUSER = "extra_iduser"
        const val EXTRA_NAME = "extra_name"
    }
}