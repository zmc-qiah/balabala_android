package org.qiah.balabala.MyListener

import org.qiah.balabala.bean.Nikke

interface SelectNikkeListener {
    fun select(nikke: Nikke)
    fun unSelect(nikke: Nikke)
}
