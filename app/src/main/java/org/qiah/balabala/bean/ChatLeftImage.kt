package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType

class ChatLeftImage(
    val path: String
) : MyType() {
    var nikke: Nikke? = null
    constructor(path: String, nikke: Nikke) : this(path) {
        this.nikke = nikke
    }
    override fun viewType(): Int = MyType.CHAT_LEFT_IMAGE
}
