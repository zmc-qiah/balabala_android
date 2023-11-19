package org.qiah.balabala.bean

class ChatLeftText(
    message: Message,
    val text: String
) : ChatItem(message) {
    var nikke: Nikke? = null
    constructor(message: Message, text: String, nikke: Nikke?) : this(message, text) {
        this.nikke = nikke
    }
    override fun viewType(): Int = CHAT_LEFT_TEXT
}
