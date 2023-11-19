package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType

abstract class ChatItem(val message: Message, val s: String = "") : MyType(s)
