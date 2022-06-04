package com.dicoding.capstones.data

data class ChatListModel(
    val orderId: String? = null,
    val id: String? = null,
    val photo: String? = null,
    val name: String? = null,
    val subject: String? = null,
    val lastMessages: String? = null,
    val unseenMessages: Int? = null,
)