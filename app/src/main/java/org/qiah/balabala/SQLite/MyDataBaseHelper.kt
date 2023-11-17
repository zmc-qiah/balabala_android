package org.qiah.balabala.SQLite

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.R
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.util.ResourceUtil
import org.qiah.balabala.util.toast
import java.io.BufferedReader
import java.io.InputStreamReader

class MyDataBaseHelper(val context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {
    private val gson: Gson by lazy {
        GsonBuilder().create()
    }
    private val createSQL = "create table Nikke(\n" +
        "\tid integer primary key autoincrement,\n" +
        "\tname text default '',\n" +
        "\tavatar text,\n" +
        "\tenterprise text default '',\n" +
        "\temoji text default ''\n" +
        ");"
    private val cms = "create table chat(\n" +
        "\tid integer primary key autoincrement,\n" +
        "\ttype integer default 1,\n" +
        "\tnikkeid integer default 0,\n" +
        "\tcontent text default '',\n" +
        "\tposition integer default 0\n" +
        ")"
    private val ccs = "create table chat(\n" +
        "\tid integer primary key autoincrement,\n" +
        "\tavatar text,\n" +
        "\tname text,\n" +
        "\tnews text,\n" +
        "\tmessgae text\n" +
        ");"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createSQL)
        db?.execSQL(ccs)
        db?.execSQL(cms)
        initNikke(db)
        "初始化数据成功".toast()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    @SuppressLint("Range")
    private fun initNikke(db: SQLiteDatabase?) {
        db?.let {
            val input = BaseApplication.context().assets.open("nikke.txt")
            val reader = BufferedReader(InputStreamReader(input))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val split = line!!.split(";")
                if (split.size >= 3) {
                    db.execSQL(
                        "insert into Nikke(name,avatar,enterprise,emoji) values(?,?,?,?)",
                        arrayOf(
                            split[1],
                            "${ResourceUtil.getString(R.string.base_url)}${ResourceUtil.getString(R.string.nikke)}${split[0]}",
                            split[2],
                            ""
                        )
                    )
                }
            }
            reader.close()
        }
    }

    @SuppressLint("Range")
    fun selectNikkeAll(): ArrayList<Nikke> {
        val list = ArrayList<Nikke>()
        val c = this.readableDatabase.rawQuery("select * from Nikke", null)
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val enterprise = c.getString(c.getColumnIndex("enterprise"))
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val nikke = Nikke(name, enterprise, avatar, id)
                val emoji = c.getString(c.getColumnIndex("emoji"))
                if (!emoji.isNullOrEmpty()) {
                    nikke.enoji = gson.fromJson<ArrayList<String>>(emoji, object : TypeToken<ArrayList<String>>() {}.type)
                }
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun selectNikkeByName(name: String): ArrayList<Nikke> {
        val list = ArrayList<Nikke>()
        val c = this.readableDatabase.rawQuery("select * from Nikke where name like ?", arrayOf("%" + name + "%"))
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val enterprise = c.getString(c.getColumnIndex("enterprise"))
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val nikke = Nikke(name, enterprise, avatar, id)
                val emoji = c.getString(c.getColumnIndex("emoji"))
                if (!emoji.isNullOrEmpty()) {
                    nikke.enoji = gson.fromJson<ArrayList<String>>(emoji, object : TypeToken<ArrayList<String>>() {}.type)
                }
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun selectNikkeById(id: Int): ArrayList<Nikke> {
        val list = ArrayList<Nikke>()
        val c = this.readableDatabase.rawQuery("select * from Nikke where id =  ?", arrayOf(id.toString()))
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val enterprise = c.getString(c.getColumnIndex("enterprise"))
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val nikke = Nikke(name, enterprise, avatar, id)
                val emoji = c.getString(c.getColumnIndex("emoji"))
                if (!emoji.isNullOrEmpty()) {
                    nikke.enoji = gson.fromJson<ArrayList<String>>(emoji, object : TypeToken<ArrayList<String>>() {}.type)
                }
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun selectNikkeByEnterprise(enterprise: String): ArrayList<Nikke> {
        val list = ArrayList<Nikke>()
        val c = this.readableDatabase.rawQuery("select * from Nikke where enterprise = ?", arrayOf(enterprise))
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val enterprise = c.getString(c.getColumnIndex("enterprise"))
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val nikke = Nikke(name, enterprise, avatar, id)
                val emoji = c.getString(c.getColumnIndex("emoji"))
                if (!emoji.isNullOrEmpty()) {
                    nikke.enoji = gson.fromJson<ArrayList<String>>(emoji, object : TypeToken<ArrayList<String>>() {}.type)
                }
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun selectAllChat(): ArrayList<Chat> {
        val list = ArrayList<Chat>()
        val c = this.readableDatabase.rawQuery("select * from chat", null)
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val news = c.getString(c.getColumnIndex("news"))
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val messgae = c.getString(c.getColumnIndex("messgae"))
                val ids = gson.fromJson<ArrayList<Int>>(messgae, object : TypeToken<ArrayList<Int>>() {}.type)
                val nikke = Chat(id, name, avatar, news, messgae, ids)
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }
}
