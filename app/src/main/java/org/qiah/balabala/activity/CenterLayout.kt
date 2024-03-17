package org.qiah.balabala.activity

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import org.qiah.balabala.util.dp
import kotlin.math.max

class CenterLayout(context: Context, attributeSet: AttributeSet) : ViewGroup(context,attributeSet) {
    // 定义一个map来记录子view的位置，key是行，value是这一行的子view的位置
    val childBondMap = LinkedHashMap<Int, ArrayList<Rect>>()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthUsed = 0
        var heightUsed = 0
        val size = MeasureSpec.getSize(widthMeasureSpec)
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        var raw = 0
        var col = 0
        var maxHeight = 0
        // 我们要让子view居中展示，因此我们父view的宽度是和子view无关的，先自己测一下
        //        val width = when(mode){
//            MeasureSpec.EXACTLY -> size
//            MeasureSpec.AT_MOST -> max(getScreenWidth(),size)
//            else -> getScreenWidth()
//        }
        val width = resolveSize(context.resources.displayMetrics.widthPixels, widthMeasureSpec)
        for (child in children) {
            // 测量
            // 这是参数的widthUsed和heightUsed 等于0就是让子view放开了测量，别比我大就行
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            // 记录使用了多高
            maxHeight = max(maxHeight, heightUsed + child.measuredHeight)
            // 判断要不要换行
            if (widthUsed + child.measuredWidth > width ) {
                // 如果换行那么已经使用了的宽高就改变了
                // 重置一下
                heightUsed = maxHeight
                widthUsed = 0
                raw++
                col = 0
                maxHeight = max(maxHeight, heightUsed + child.measuredHeight)
            }
            if (childBondMap[raw] == null) {
                childBondMap[raw] = ArrayList()
            }
            if (childBondMap[raw]!!.size <= col) {
                childBondMap[raw]!!.add(Rect())
            }
            childBondMap[raw]!![col].set(widthUsed, heightUsed, widthUsed + child.measuredWidth, heightUsed + child.measuredHeight)
            col++
            widthUsed += child.measuredWidth
        }
        setMeasuredDimension(size, maxHeight)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var index = 0
        val c = children.toList()
        val w = r - l
        for (i in 0 until childBondMap.size) {
            val list = childBondMap[i]
            list?.let { list ->
                var used = 0
                list.forEach {
                    used += it.right - it.left
                }
                val offset = (w - used) / (list.size + 1 )
                for (j in 0 until list.size) {
                    val rect = list[j]
                    c[index++].layout(rect.left + offset * (j + 1), rect.top, rect.right + offset * (j + 1), rect.bottom)
                }
            }

        }
    }
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}