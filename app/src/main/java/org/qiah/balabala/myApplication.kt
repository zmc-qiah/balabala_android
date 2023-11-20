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
        return "SourceHanSansCN-Normal#1.otf"
    }
    override fun blodPath(): String {
        return "SourceHanSansCN-Heavy#1.otf"
    }
}
