package org.jxxy.debug.sensors

import android.view.View

class SensorsClickListener(val before: (v: View?) -> Unit, val after: (v: View?) -> Unit, val oldListener: View.OnClickListener? = null) : View.OnClickListener {
    override fun onClick(v: View?) {
        before(v)
        oldListener?.onClick(v)
        after(v)
    }
}
