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
    var tememoji: String = ""
    var id: Int = 0
    var flag = false
    constructor(name: String, enterprise: String, avatarId: Int?) : this(name, enterprise) {
        this.avatarId = avatarId
    }
    constructor(name: String, enterprise: String, avatarPath: String?) : this(name, enterprise) {
        this.avatarPath = avatarPath
    }
    constructor(name: String, enterprise: String, avatarPath: String?, id: Int, tememoji: String = "") : this(name, enterprise, avatarPath) {
        this.id = id
        this.tememoji = tememoji
    }
    override fun viewType(): Int = NIKKE
}
