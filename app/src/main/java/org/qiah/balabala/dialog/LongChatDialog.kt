package org.qiah.balabala.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.qiah.balabala.MyListener.ClickLongChatDialogListener
import org.qiah.balabala.R
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.databinding.DialogMofidyBinding
import org.qiah.balabala.util.ResourceUtil
import org.qiah.balabala.util.dp
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.singleClick

class LongChatDialog(val location: IntArray, val position: Int, val entry: Chat, val listener: ClickLongChatDialogListener) : BaseDialog<DialogMofidyBinding>() {
    init {
        enableBack = true
        ifCancelOnTouch = true
        width = 170.dp()
        height = 60.dp()
        gravity = Gravity.START.or(Gravity.TOP)
        alpha = 0f
    }
    override fun initView(view: DialogMofidyBinding) {
        view.modifyView.gone()
        view.updateTextView.gone()
        view.updateIcon.gone()
        view.insertTv.text = "置顶"
        view.insertIcon.text = ResourceUtil.getString(R.string.icon_move_top)
        view.root.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val lp = dialog?.window?.attributes
        lp?.let {
            it.x = getWidth() / 3 * 2 - width / 2
            it.y = location[1] - this.height
        }
        view.insertView.singleClick {
            listener.onMoveTo(entry, position)
            dismiss()
        }
        view.deleteView.singleClick {
            listener.onDelete(entry, position)
            dismiss()
        }
        view.modifyView.singleClick {
            listener.onDelete(entry, position)
            dismiss()
        }
    }

    override fun getView(inflater: LayoutInflater, parent: ViewGroup?): DialogMofidyBinding = DialogMofidyBinding.inflate(layoutInflater)
}