package org.qiah.balabala.SQLite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getIntOrNull
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.R
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.bean.Message
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

    private val ccs = "create table chat(\n" +
        "\tid integer primary key autoincrement,\n" +
        "\tavatar text,\n" +
        "\tname text,\n" +
        "\tnikkeids text,\n" +
        "\tnews text\n" +
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
        val c = this.rd.rawQuery("select * from Nikke", null)
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
        val c = this.rd.rawQuery("select * from Nikke where name like ?", arrayOf("%" + name + "%"))
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
    fun selectNikkeById(id: Int): Nikke? {
        var nikke: Nikke? = null
        val c = this.rd.rawQuery("select * from Nikke where id =  ?", arrayOf(id.toString()))
        if (c.moveToFirst()) {
            val name = c.getString(c.getColumnIndex("name"))
            val avatar = c.getString(c.getColumnIndex("avatar"))
            val enterprise = c.getString(c.getColumnIndex("enterprise"))
            val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
            nikke = Nikke(name, enterprise, avatar, id)
            val emoji = c.getString(c.getColumnIndex("emoji"))
            if (!emoji.isNullOrEmpty()) {
                nikke.enoji = gson.fromJson<ArrayList<String>>(emoji, object : TypeToken<ArrayList<String>>() {}.type)
            }
        }
        c.close()
        return nikke
    }

    @SuppressLint("Range")
    fun selectNikkeByIds(ids: ArrayList<Int>): ArrayList<Nikke> {
        val list = ArrayList<Nikke>()
        val c = this.rd.rawQuery("select * from Nikke where id in (${ids.joinToString()}) ", null)
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
        val c = this.rd.rawQuery("select * from Nikke where enterprise = ?", arrayOf(enterprise))
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
        val c = this.rd.rawQuery("select * from chat", null)
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val news = c.getString(c.getColumnIndex("news"))
                val nikkeids = c.getString(c.getColumnIndex("nikkeids"))
                val ids = gson.fromJson<ArrayList<Int>>(
                    nikkeids,
                    object : TypeToken<ArrayList<Int>>() {}.type
                )
                val nikkes = selectNikkeByIds(ids)
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val nikke = Chat(id, name, avatar, news, ids, nikkes)
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun selectChatById(id: Int): Chat? {
        var res: Chat? = null
        val c = this.rd.rawQuery("select * from chat where id = ?", arrayOf(id.toString()))
        if (c.moveToFirst()) {
            val name = c.getString(c.getColumnIndex("name"))
            val avatar = c.getString(c.getColumnIndex("avatar"))
            val news = c.getString(c.getColumnIndex("news"))
            val nikkeids = c.getString(c.getColumnIndex("nikkeids"))
            val ids = gson.fromJson<ArrayList<Int>>(
                nikkeids,
                object : TypeToken<ArrayList<Int>>() {}.type
            )
            val nikkes = selectNikkeByIds(ids)
            val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
            res = Chat(id, name, avatar, news, ids, nikkes)
        }
        c.close()
        return res
    }

    @SuppressLint("Range")
    fun insertChat(chat: Chat): Int {
        Log.d("TAG", "selectAllexceptionChat:" + gson.toJson(chat.nikkeIds))
        return this.wd.insert(
            "chat",
            null,
            ContentValues().apply {
                put("name", chat.name)
                put("avatar", chat.avatar)
                put("news", chat.news)
                put("nikkeids", gson.toJson(chat.nikkeIds))
            }
        ).toInt()
    }
    fun updateChat(chat: Chat) {
        wd.update(
            "chat",
            ContentValues().apply {
                put("name", chat.name)
                put("avatar", chat.avatar)
                put("news", chat.news)
                put("nikkeids", gson.toJson(chat.nikkeIds))
            },
            "id = ?",
            arrayOf(chat.id.toString())
        )
    }
    private val cms = "create table message(\n" +
        "\tid integer primary key autoincrement,\n" +
        "\tchatid integer default 1,\n" +
        "\ttype integer default 1,\n" +
        "\tnikkeid integer default 0,\n" +
        "\tcontent text default '',\n" +
        "\tposition integer default 0\n" +
        ")"
    fun updateMessage(message: Message) {
        Log.d("addMessages", "updateMessage: " + message.postion + message.content)
        wd.update(
            "message",
            ContentValues().apply {
                put("chatid", message.chatId)
                put("type", message.type)
                put("nikkeid", message.nikkeId)
                put("content", message.content)
            },
            "id = ?",
            arrayOf(message.id.toString())
        )
    }

    @SuppressLint("Range")
    fun deleteMessage(message: Message) {
        val c = this.rd.rawQuery("select * from message where chatid = ? and position > ? ", arrayOf(message.chatId.toString(), (message.postion).toString()))
        if (c.moveToFirst()) {
            do {
                val id = c.getInt(c.getColumnIndex("id"))
                val np = c.getInt(c.getColumnIndex("position")) - 1
                wd.execSQL("UPDATE message SET position = ? WHERE id = ?;", arrayOf(np, id))
                Log.d("addMessages", "selectAllMessageByChatId: " + np)
            } while (c.moveToNext())
        }
        wd.delete(
            "message",
            "id = ?",
            arrayOf(message.id.toString())
        )
    }

    @SuppressLint("Range")
    fun selectAllMessageByChatId(id: Int): ArrayList<Message> {
        val list = ArrayList<Message>()
        val c = this.rd.rawQuery("select * from message where chatid = ? ", arrayOf(id.toString()))
        if (c.moveToFirst()) {
            do {
                val id = c.getInt(c.getColumnIndex("id"))
                val chatid = c.getInt(c.getColumnIndex("chatid"))
                val type = c.getInt(c.getColumnIndex("type"))
                val nikkeid = c.getInt(c.getColumnIndex("nikkeid"))
                val position = c.getInt(c.getColumnIndex("position"))
                Log.d("addMessages", "selectAllMessageByChatId: " + position)
                val content = c.getString(c.getColumnIndex("content"))
                val message = Message(id, chatid, nikkeid, type, content, position)
                list.add(message)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    private val rd by lazy { this.readableDatabase }
    private val wd by lazy { this.writableDatabase }

    @SuppressLint("Range")
    fun insertMessage(message: Message): Int {
        Log.d("addMessages", "insertMessage: " + message.postion + message.content)
        val c = this.rd.rawQuery("select * from message where chatid = ? and position > ? ", arrayOf(message.chatId.toString(), (message.postion - 1).toString()))
        if (c.moveToFirst()) {
            do {
                val id = c.getInt(c.getColumnIndex("id"))
                val np = c.getInt(c.getColumnIndex("position")) + 1
                wd.execSQL("UPDATE message SET position = ? WHERE id = ?;", arrayOf(np, id))
                Log.d("addMessages", "selectAllMessageByChatId: " + np)
            } while (c.moveToNext())
        }
        c.close()
        val a = this.wd.insert(
            "message",
            null,
            ContentValues().apply {
                put("chatid", message.chatId)
                put("type", message.type)
                put("nikkeid", message.nikkeId)
                put("position", message.postion)
                put("content", message.content)
            }
        ).toInt()
        return a
    }
}
