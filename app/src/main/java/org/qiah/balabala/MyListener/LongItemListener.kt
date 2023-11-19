package org.qiah.balabala.MyListener

import org.qiah.balabala.util.MyType

interface LongItemListener {
    fun onLongClick(location: IntArray, position: Int, entry: MyType)
}
