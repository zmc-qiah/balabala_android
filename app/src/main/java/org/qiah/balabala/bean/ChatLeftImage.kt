package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType

class ChatLeftImage(
    message: Message,
    val path: String
) : ChatItem(message) {
    var nikke: Nikke? = null
    constructor(message: Message, path: String, nikke: Nikke?) : this(message, path) {
        this.nikke = nikke
    }
    override fun viewType(): Int = MyType.CHAT_LEFT_IMAGE
}
