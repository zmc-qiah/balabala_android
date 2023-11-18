package org.qiah.balabala.MyListener

interface ClickItemListener<T> {
    fun onClick(data: T)
    fun onClick(data: T, position: Int)
}
