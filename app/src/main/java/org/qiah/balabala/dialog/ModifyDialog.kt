package org.qiah.balabala.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.qiah.balabala.MyListener.ClickModifyDialogListener
import org.qiah.balabala.bean.ChatItem
import org.qiah.balabala.databinding.DialogMofidyBinding
import org.qiah.balabala.util.dp
import org.qiah.balabala.util.enen
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.singleClick

class ModifyDialog(val location: IntArray, val listener: ClickModifyDialogListener, val entry: ChatItem, val position: Int, val a: () -> Unit) : BaseDialog<DialogMofidyBinding>() {
    init {
        enableBack = true
        ifCancelOnTouch = true
        width = 170.dp()
        height = 60.dp()
        gravity = Gravity.START.or(Gravity.TOP)
        alpha = 0f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val lp = dialog?.window?.attributes
        lp?.let {
            it.x = getWidth() / 3 * 2 - width / 2
            it.y = location[1] - this.height
        }
    }

    override fun initView(view: DialogMofidyBinding) {
        view.deleteView.singleClick {
            listener.onDelete(entry, position)
            enen()
            dismiss()
        }
        view.modifyView.singleClick {
            listener.onUpdate(entry, position)
            enen()
            dismiss()
        }
        view.insertView.singleClick {
            listener.onInsert(entry, position)
            enen()
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        a()
    }

    override fun getView(inflater: LayoutInflater, parent: ViewGroup?): DialogMofidyBinding = DialogMofidyBinding.inflate(layoutInflater)
}
