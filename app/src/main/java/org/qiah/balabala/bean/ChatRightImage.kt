package org.qiah.balabala.bean

class ChatRightImage(
    message: Message,
    val path: String
) : ChatItem(message) {
    override fun viewType(): Int = CHAT_RIGHT_IMAGE
}
