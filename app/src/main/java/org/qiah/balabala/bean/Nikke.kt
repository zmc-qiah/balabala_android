package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType
import java.io.Serializable

data class Nikke(
    val name: String,
    val enterprise: String
) : MyType(), Serializable {
    var avatarId: Int? = null
    var avatarPath: String? = null
    var enoji: ArrayList<String> ? = null
    constructor(name: String, enterprise: String, avatarId: Int?) : this(name, enterprise) {
        this.avatarId = avatarId
    }
    constructor(name: String, enterprise: String, avatarPath: String?) : this(name, enterprise) {
        this.avatarPath = avatarPath
    }
    override fun viewType(): Int = NIKKE
}
