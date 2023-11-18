package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType

data class ChatLeftText(
    val text: String
) : MyType() {
    var nikke: Nikke? = null
    constructor(text: String, nikke: Nikke?) : this(text) {
        this.nikke = nikke
    }
    override fun viewType(): Int = CHAT_LEFT_TEXT
}
