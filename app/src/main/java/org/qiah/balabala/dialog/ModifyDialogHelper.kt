package org.qiah.balabala.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import org.qiah.balabala.databinding.DialogMofidyBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getHeight
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.toast
import org.qiah.balabala.viewHolder.MultipleViewHolder

class ModifyDialogHelper(val context: Context) {
    private val window: PopupWindow
    private val tempLocation = IntArray(2)
    private val width: Int
    private val heith: Int
    private val view: DialogMofidyBinding
    init {
        view = DialogMofidyBinding.inflate(LayoutInflater.from(context))
        view.root.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        width = view.root.width
        heith = view.root.height
        window = PopupWindow(
            view.root,
            width,
            heith,
            false
        )
        view.apply {
        }
    }
    fun show(viewHolder: MultipleViewHolder<*, MyType>, entity: MyType) {
        "aaaaa".toast()
        window.showAtLocation(viewHolder.view!!.root, Gravity.NO_GRAVITY, getWidth() / 2, getHeight() / 2)
    }
    fun dismiss() {
        window.dismiss()
    }
    val isShowing: Boolean
        get() = window.isShowing
}
