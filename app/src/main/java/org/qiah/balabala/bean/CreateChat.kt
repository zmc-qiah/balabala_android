package org.qiah.balabala.bean

import java.io.Serializable

data class CreateChat(
    var nikkes: ArrayList<Nikke>,
    var name: String
) : Serializable
