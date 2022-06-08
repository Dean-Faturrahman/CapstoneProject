package com.dicoding.capstones.ui.chatroom

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.R
import com.dicoding.capstones.adapter.MessageAdapter
import com.dicoding.capstones.data.MessageModel
import com.dicoding.capstones.databinding.ActivityChattingRoomBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChattingRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChattingRoomBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private val chattingRoomViewModel by viewModels<ChattingRoomViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getName = intent.getStringExtra(EXTRA_NAME)
        val getId = intent.getStringExtra(EXTRA_ID)
        val getIdUser = intent.getStringExtra(EXTRA_IDUSER)
        val getOrderId = intent.getStringExtra(EXTRA_ORDERID)

        supportActionBar?.hide()
        binding.nameUserChat.text = getName

        actionButton(getOrderId)

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
                    Toast.makeText(this, "Pesan Terkirim", Toast.LENGTH_SHORT).show()
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

    private fun actionButton(orderId: String?) {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEnd.setOnClickListener {
            showBottomSheetRating(orderId)
        }
    }

    private fun completeOrder(orderId: String, rating: Float?, desc: String) {
        chattingRoomViewModel.completeOrder(orderId)
        chattingRoomViewModel.orderComplete.observe(this) {
            if (it.status == 1) {
                chattingRoomViewModel.orderRating(orderId, rating, desc)
                db.reference.child("chat").child(orderId).child("status").setValue("Completed")
                finish()
            }
        }
    }

    private fun showBottomSheetRating(orderId: String?) {
        val dialog = BottomSheetDialog(this@ChattingRoomActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_rating, null)
        val ratingBar = view.findViewById<RatingBar>(R.id.rating_bar)
        val ratingDesc = view.findViewById<EditText>(R.id.desc_rating)
        val butSkip = view.findViewById<Button>(R.id.btn_rating_skip)
        val butEnd = view.findViewById<Button>(R.id.btn_rating_end)

        var rating: Float? = null

        ratingBar.setOnRatingBarChangeListener { rat, _, _ ->
            rating = rat.rating
        }

        butEnd.setOnClickListener {
            val desc = ratingDesc.text.toString()
            if (orderId != null) {
                completeOrder(orderId, rating, desc)
            }
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
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