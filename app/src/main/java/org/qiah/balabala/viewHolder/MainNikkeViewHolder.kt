package org.qiah.balabala.viewHolder

import org.qiah.balabala.bean.MainNikke
import org.qiah.balabala.databinding.ItemNikkeMainBinding

class MainNikkeViewHolder(binding: ItemNikkeMainBinding) : MultipleViewHolder<ItemNikkeMainBinding, MainNikke>(binding) {
    override fun setHolder(entity: MainNikke) {
        view.nikkeIv.setImageDrawable(entity.drawable)
        view.nikkeTv.text = entity.name
        view.messageTv.text = entity.message
    }
}
