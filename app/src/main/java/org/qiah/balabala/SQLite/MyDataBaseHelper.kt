package org.qiah.balabala.SQLite

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.qiah.balabala.util.toast

class MyDataBaseHelper(val context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {
    private val createSQL = "create table Nikke(\n" +
        "\tid integer primary key autoincrement,\n" +
        "\tname text default '',\n" +
        "\tavatar text,\n" +
        "\tenterprise text default '',\n" +
        "\temoji text default ''\n" +
        ");"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createSQL)
        initNikke(db)
        "name".toast()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    @SuppressLint("Range")
    private fun initNikke(db: SQLiteDatabase?) {
        db?.let {
            db.execSQL(
                "insert into Nikke(name,avatar,enterprise,emoji) values(?,?,?,?)",
                arrayOf(
                    "https://nikke-balabala.obs.cn-east-3.myhuaweicloud.com/nikke/als.png",
                    "爱丽丝",
                    "泰特拉",
                    ""
                )
            )
            val c = db.rawQuery("select * from Nikke", null)
            if (c.moveToFirst()) {
                do {
                    var name = c.getString(c.getColumnIndex("name"))
                    var avatar = c.getString(c.getColumnIndex("avatar"))
                    Log.d("rawQuery", "onCreate: " + name + avatar)
                } while (c.moveToNext())
            }
        }
    }
}
