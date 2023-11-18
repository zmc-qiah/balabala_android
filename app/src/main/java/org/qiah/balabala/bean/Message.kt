package org.qiah.balabala.bean

data class Message(
    var id: Int,
    val chatId: Int,
    val nikkeId: Int,
    val type: Int,
    val content: String,
    val postion: Int
)
