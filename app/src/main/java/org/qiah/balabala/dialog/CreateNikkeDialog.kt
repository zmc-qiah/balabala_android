package org.qiah.balabala.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import org.qiah.balabala.MyListener.ClickItemListener
import org.qiah.balabala.databinding.DialogAddNikkeBinding
import org.qiah.balabala.util.dp
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.util.toast

class CreateNikkeDialog(val listener: ClickItemListener<String>) : BaseDialog<DialogAddNikkeBinding>() {
    init {
        gravity = Gravity.CENTER
        enableBack = true
        height = 50.dp()
        width = ViewGroup.LayoutParams.WRAP_CONTENT
    }
    override fun initView(view: DialogAddNikkeBinding) {
        view.searchBtn.singleClick {
            val s = view.searchEt.text.toString()
            if (s.isNullOrEmpty()) {
                "请输入".toast()
            } else {
                listener.onClick(s)
                dismiss()
            }
        }
    }

    override fun getView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DialogAddNikkeBinding = DialogAddNikkeBinding.inflate(layoutInflater)
}
