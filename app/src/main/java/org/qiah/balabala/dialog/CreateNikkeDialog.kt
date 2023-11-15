package org.qiah.balabala.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.MyListener.ClickCreateListener
import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.R
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.DialogCreateNikkeChatBinding
import org.qiah.balabala.databinding.ItemSelectNikkeBinding
import org.qiah.balabala.util.SpanItemDecoration
import org.qiah.balabala.util.getHeight
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.viewHolder.NikkeAvatarViewHolder

class CreateNikkeDialog(var listener: ClickCreateListener) : BaseDialog<DialogCreateNikkeChatBinding>() {
    init {
        gravity = Gravity.BOTTOM
        enableBack = true
        height = getHeight() / 6 * 5
        alpha = 0.3f
    }
    private val SelectNikkeListener by lazy {
        object : SelectNikkeListener {
            override fun select(nikke: Nikke) {
                nikkes.add(nikke)
            }

            override fun unSelect(nikke: Nikke) {
                nikkes.remove(nikke)
            }
        }
    }
    private val nikkes by lazy {
        ArrayList<Nikke>()
    }
    private val adapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = NikkeAvatarViewHolder(ItemSelectNikkeBinding.inflate(layoutInflater, viewGroup, false), SelectNikkeListener)
        }
    }
    override fun initView(view: DialogCreateNikkeChatBinding) {
        view.nikkeAvatarRv.adapter = adapter
        view.nikkeAvatarRv.addItemDecoration(SpanItemDecoration(8F, 4F, 4))
        view.createIv.singleClick {
            listener.onClick(nikkes)
            dismiss()
        }
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
        adapter.add(Nikke("红莲", "", R.drawable.chli))
    }
    override fun getView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DialogCreateNikkeChatBinding = DialogCreateNikkeChatBinding.inflate(layoutInflater)
}
