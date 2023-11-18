package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType
import java.io.Serializable

data class Chat(
    var id: Int,
    val name: String,
    val avatar: String,
    var news: String,
    val nikkeIds: ArrayList<Int>,
    val nikkes: ArrayList<Nikke>
) : MyType(), Serializable {
    override fun viewType(): Int = MyType.CHAT
}
