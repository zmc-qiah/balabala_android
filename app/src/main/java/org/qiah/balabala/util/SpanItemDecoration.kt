package org.qiah.balabala.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
/**
 * @param intervalVertical 纵向间距，从第二行才有的向上距离，单位为dp，不需要转化
 * @param intervalHorizontal 横向间距，，单位为dp，不需要转化
 * @param spanCount 横向网格数量，即一行有几列
 */
class SpanItemDecoration(
    private val intervalVertical: Float,
    private val intervalHorizontal: Float,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {
    private val halfIntervalHorizontal = intervalHorizontal / 2f

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position / spanCount == 0) {
            // 首行
        } else {
            outRect.top = intervalVertical.dp()
        }
        outRect.left = halfIntervalHorizontal.dp()
        outRect.right = halfIntervalHorizontal.dp()
    }
}
