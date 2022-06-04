package com.dicoding.capstones.data

data class MessageModel(
    val text: String? = null,
    val id: String? = null
) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}