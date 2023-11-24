package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType
import java.io.Serializable

data class Chat(
    var id: Int,
    val name: String,
    val avatar: String,
    var news: String,
    var position: Int,
    var nikkeIds: ArrayList<Int>?,
    var nikkes: ArrayList<Nikke>?
) : MyType(), Serializable {
    var tempIds: String = ""
    constructor(id: Int, name: String, avatar: String, news: String, position: Int, tempIds: String) : this(id, name, avatar, news, position, null, null) {
        this.tempIds = tempIds
    }
    override fun viewType(): Int = MyType.CHAT
}
