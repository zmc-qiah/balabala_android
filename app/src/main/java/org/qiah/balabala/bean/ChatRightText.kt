package org.qiah.balabala.bean

class ChatRightText(
    message: Message,
    val text: String
) : ChatItem(message) {
    override fun viewType(): Int = CHAT_RIGHT_TEXT
}
