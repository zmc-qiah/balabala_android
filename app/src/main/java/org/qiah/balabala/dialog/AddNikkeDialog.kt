package org.qiah.balabala.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.MyListener.AddNikkeListener
import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.DialogCreateNikkeChatBinding
import org.qiah.balabala.databinding.ItemSelectNikkeBinding
import org.qiah.balabala.util.SpanItemDecoration
import org.qiah.balabala.util.getHeight
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.util.toast
import org.qiah.balabala.viewHolder.NikkeAvatarViewHolder

class AddNikkeDialog(var listener: AddNikkeListener) : BaseDialog<DialogCreateNikkeChatBinding>() {
    init {
        gravity = Gravity.BOTTOM
        enableBack = true
        height = getHeight()
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
    private val db by lazy {
        MyDataBaseHelper(BaseApplication.context(), "Nikke_balabala", 1)
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
        view.nameEt.gone()
        view.nameIv.gone()
        view.aTv.text = "添加"
        view.nikkeAvatarRv.addItemDecoration(SpanItemDecoration(8F, 4F, 4))
        view.createIv.singleClick {
            if (nikkes.size != 0) {
                listener.onClick(nikkes)
                dismiss()
            } else {
                "请选择Nikke".toast()
            }
        }
        adapter.add(db.selectNikkeAll())
        view.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val s1 = s.toString()
                if (s1.isEmpty()) {
                    adapter.clearAndAdd(db.selectNikkeAll())
                } else {
                    if ("米西利斯".equals(s1) || "泰特拉".equals(s1) || "极乐净土".equals(s1) || "其他".equals(s1) || "朝圣者".equals(s1)) {
                        adapter.clearAndAdd(db.selectNikkeByEnterprise(s1))
                    } else {
                        adapter.clearAndAdd(db.selectNikkeByName(s1))
                    }
                }
            }
        })
    }
    override fun getView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DialogCreateNikkeChatBinding = DialogCreateNikkeChatBinding.inflate(layoutInflater)
}
