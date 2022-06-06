package com.dicoding.capstones.ui.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstones.adapter.ChatAdapter
import com.dicoding.capstones.data.ChatListModel
import com.dicoding.capstones.databinding.FragmentTeacherChatBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.ui.chatroom.ChattingRoomActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TeacherChatFragment : Fragment() {

    private val listChat = ArrayList<ChatListModel>()
    private lateinit var db: FirebaseDatabase
    private var _binding: FragmentTeacherChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref : PrefHelper
    private var lastMessage : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeacherChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = PrefHelper(requireContext())
        db = Firebase.database

        val layoutManager = LinearLayoutManager(context)
        binding.rvListChat.layoutManager = layoutManager

        val listChatAdapter = ChatAdapter(listChat)
        binding.rvListChat.adapter = listChatAdapter

        listChatAdapter.setOnItemClickCallback(object : ChatAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ChatListModel) {
                val toRoomChat = Intent(requireActivity(), ChattingRoomActivity::class.java)
                toRoomChat.putExtra(ChattingRoomActivity.EXTRA_NAME, data.name)
                toRoomChat.putExtra(ChattingRoomActivity.EXTRA_ID, data.id)
                toRoomChat.putExtra(ChattingRoomActivity.EXTRA_ORDERID, data.orderId)
                toRoomChat.putExtra(ChattingRoomActivity.EXTRA_IDUSER, sharedPref.getString(Const.PREF_USERID))
                startActivity(toRoomChat)
            }
        })

        db.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()

                db.reference.child("chat").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.childrenCount > 0) {
                            Log.e("CheckCount", snapshot.childrenCount.toString())

                            for (snapshot1 in snapshot.children) {
                                val getTeacherId = snapshot1.child("teacher").child("id").value
                                val getStatusOrder = snapshot1.child("status").value.toString()

                                if (getTeacherId == sharedPref.getString(Const.PREF_USERID) && getStatusOrder == "On Going") {
                                    val getOrderId = snapshot1.key
                                    val getStudentId = snapshot1.child("student").child("id").value
                                    val getStudentName = snapshot1.child("student").child("name").value
                                    val getStudentPhoto = snapshot1.child("student").child("photo").value
                                    val getSubject = snapshot1.child("teacher").child("subject").value

                                    for (chatSnapshot in snapshot1.child("messages").children) {
                                        lastMessage = chatSnapshot.child("text").value.toString()
                                    }

                                    val chatListModel = ChatListModel(getOrderId.toString(), getStudentId.toString(), getStudentPhoto.toString(), getStudentName.toString(), getSubject.toString() ,lastMessage)
                                    lastMessage = null
                                    listChat.add(chatListModel)
                                    listChatAdapter.updateData(listChat)
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}