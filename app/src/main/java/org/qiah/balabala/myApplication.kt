package org.qiah.balabala

import org.qiah.balabala.SQLite.MyDataBaseHelper

class myApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        MyDataBaseHelper(context(), "Nikke_balabala", 1)
    }

    public override fun iconFontPath(): String {
        return "iconfont.ttf"
    }

    override fun normalPath(): String {
        return "sourcehansans.ttf"
    }
}
