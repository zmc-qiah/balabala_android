package org.qiah.balabala.dialog

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.qiah.balabala.MyListener.ClickPreviewEmojiDialogListener
import org.qiah.balabala.databinding.DialogPreviewEmojiBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.dp
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.load
import org.qiah.balabala.util.singleClick

class PreviewEmojiDialog(val location: IntArray, val entry: MyType, val listener: ClickPreviewEmojiDialogListener) : BaseDialog<DialogPreviewEmojiBinding>() {
    private val TAG = "PreviewEmojiD"
    init {
        enableBack = true
        ifCancelOnTouch = true
        width = (getWidth() * 0.35).toInt()
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        gravity = Gravity.START.or(Gravity.TOP)
        alpha = 0f
    }
    override fun initView(view: DialogPreviewEmojiBinding) {
        view.aIv.load(entry.data, 12)
        view.aIv.layoutParams.width = (getWidth() * 0.35).toInt()
        view.aIv.layoutParams.height = (getWidth() * 0.35).toInt()
        view.aIv.scaleType = ImageView.ScaleType.CENTER_CROP
        view.root.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        var x1 = location[0] - Math.abs(view.root.measuredWidth - location[2]) / 2
        val dp = 10.dp<Int>()
        if (x1 > getWidth()) {
            x1 = getWidth()
        } else if (x1 < 0) {
            x1 = dp
        }
        dialog?.window?.attributes?.let {
            it.x = x1
            it.y = (location[1] - view.root.measuredHeight - dp)
        }
        Log.d(TAG, "${getWidth()}+initView: " + dialog?.window?.attributes?.x + " " + dialog?.window?.attributes?.y + " " + view.root.measuredWidth + " " + view.root.measuredHeight)
        view.aIv.singleClick {
            listener.onClock(entry)
            dismiss()
        }
        view.deleteTv.singleClick {
            listener.onClockDelete(entry)
            dismiss()
        }
        view.moveTopTv.singleClick {
            listener.onClockMoveTop(entry)
            dismiss()
        }
    }

    override fun getView(inflater: LayoutInflater, parent: ViewGroup?): DialogPreviewEmojiBinding = DialogPreviewEmojiBinding.inflate(layoutInflater)
}
