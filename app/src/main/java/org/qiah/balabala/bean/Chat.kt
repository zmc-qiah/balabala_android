package org.qiah.balabala.bean

data class Chat(
    val id: Int,
    val name: String,
    val avatar: String,
    val news: String,
    val messages: String,
    val ids: ArrayList<Int>
)
