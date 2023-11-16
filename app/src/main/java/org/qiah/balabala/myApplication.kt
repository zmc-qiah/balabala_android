package org.qiah.balabala

import android.util.Log
import org.qiah.balabala.SQLite.MyDataBaseHelper

class myApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        val dp = MyDataBaseHelper(context(), "Nikke_balabala", 1)

    }

    public override fun iconFontPath(): String {
        return "iconfont.ttf"
    }

    override fun normalPath(): String {
        return "sourcehansans.ttf"
    }
}
