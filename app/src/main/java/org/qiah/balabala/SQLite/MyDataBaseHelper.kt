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
import com.luck.picture.lib.entity.LocalMedia
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
        "\tnews text,\n" +
        "\tposition integer\n" +
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
            db.execSQL(
                "insert into Nikke(id,name,avatar,enterprise,emoji) values(?,?,?,?,?)",
                arrayOf(
                    "0",
                    "指挥官",
                    "${ResourceUtil.getString(R.string.base_url)}${ResourceUtil.getString(R.string.zhi)}",
                    "其他",
                    ""
                )
            )
            while (reader.readLine().also { line = it } != null) {
                val split = line!!.split(";")
                if (split.size >= 3) {
                    db.execSQL(
                        "insert into Nikke(name,avatar,enterprise,emoji) values(?,?,?,?)",
                        arrayOf(
                            split[1],
                            "${ResourceUtil.getString(R.string.base_url)}${ResourceUtil.getString(R.string.nikke)}${split[0]}",
                            split[2],
                            if (split.size > 3) split[3] else ""
                        )
                    )
                }
            }
            reader.close()
        }
    }

    fun addNikke(name: String, avatar: String): Int = wd.insert(
        "Nikke",
        null,
        ContentValues().apply {
            put("name", name)
            put("avatar", avatar)
            put(
                "enterprise",
                ResourceUtil.getString(
                    R.string.added_nikke_enterprise
                )
            )
        }
    ).toInt()

    @SuppressLint("Range")
    fun selectNikkeAll(): ArrayList<Nikke> {
        val list = ArrayList<Nikke>()
        val c = this.rd.rawQuery("select * from Nikke where id > 0 ", null)
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val avatar = c.getString(c.getColumnIndex("avatar"))
                val enterprise = c.getString(c.getColumnIndex("enterprise"))
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val emoji = c.getString(4)
                if (id == 2) Log.d("TAG", "selectNikkeAll: " + emoji)
                val nikke = Nikke(name, enterprise, avatar, id, emoji)
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
                val emoji = c.getString(c.getColumnIndex("emoji"))
                val nikke = Nikke(name, enterprise, avatar, id, emoji)
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun selectEmojiByNikkeId(id: Int): List<String>? {
        var list: List<String>? = null
        val c = rd.rawQuery("select emoji from Nikke where id = ?", arrayOf(id.toString()))
        if (c.moveToFirst()) {
            val emoji = c.getString(c.getColumnIndex("emoji"))
            list = gson.fromJson(emoji, object : TypeToken<List<String>>() {})
        }
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
            val emoji = c.getString(c.getColumnIndex("emoji"))
            nikke = Nikke(name, enterprise, avatar, id, emoji)
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
                val emoji = c.getString(c.getColumnIndex("emoji"))
                val nikke = Nikke(name, enterprise, avatar, id, emoji)
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun addEmojiByNikkeId(id: Int, list: List<LocalMedia>) {
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
        nikke?.let {
            if (nikke.enoji == null) {
                nikke.enoji = ArrayList()
            }
            nikke.enoji!!.addAll(
                list.map {
                    it.realPath
                }
            )
            rd.update(
                "Nikke",
                ContentValues().apply {
                    put("emoji", gson.toJson(nikke.enoji))
                },
                "id = ?",
                arrayOf(id.toString())
            )
        }
    }
    fun updateEmojiByNikke(nikke: Nikke) {
        rd.update(
            "Nikke",
            ContentValues().apply {
                put("emoji", gson.toJson(nikke.enoji))
            },
            "id = ?",
            arrayOf(nikke.id.toString())
        )
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
                val emoji = c.getString(c.getColumnIndex("emoji"))
                val nikke = Nikke(name, enterprise, avatar, id, emoji)
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
                val id = c.getIntOrNull(c.getColumnIndex("id")) ?: 0
                val position = c.getIntOrNull(c.getColumnIndex("position")) ?: 0
                val nikke = Chat(id, name, avatar, news, position, nikkeids)
                list.add(nikke)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }

    @SuppressLint("Range")
    fun moveChatToTop(chat: Chat) {
        val c = this.rd.rawQuery(
            "select * from chat where position < ?",
            arrayOf(chat.position.toString())
        )
        val positions = ArrayList<Int>()
        val ids = ArrayList<Int>()
        if (c.moveToFirst()) {
            do {
                val position = c.getInt(c.getColumnIndex("position"))
                val id = c.getInt(c.getColumnIndex("id"))
                positions.add(position)
                ids.add(id)
            } while (c.moveToNext())
        }
        c.close()
        for (i in 0 until positions.size) {
            wd.update(
                "chat",
                ContentValues().apply {
                    put("position", positions[i] + 1)
                },
                "id = ?",
                arrayOf(ids[i].toString())
            )
        }
        wd.update(
            "chat",
            ContentValues().apply {
                put("position", 0)
            },
            "id = ?",
            arrayOf(chat.id.toString())
        )
    }

    @SuppressLint("Range")
    fun deleteChat(chat: Chat) {
        val c = this.rd.rawQuery("select * from chat where position > ?", arrayOf(chat.position.toString()))
        val positions = ArrayList<Int>()
        val ids = ArrayList<Int>()
        if (c.moveToFirst()) {
            do {
                val position = c.getInt(c.getColumnIndex("position"))
                val id = c.getInt(c.getColumnIndex("id"))
                positions.add(position)
                ids.add(id)
            } while (c.moveToNext())
        }
        c.close()
        for (i in 0 until positions.size) {
            wd.update(
                "chat",
                ContentValues().apply {
                    put("position", positions[i] - 1)
                },
                "id = ?",
                arrayOf(ids[i].toString())
            )
        }
        wd.delete("chat", "id = ?", arrayOf(chat.id.toString()))
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
            val position = c.getInt(c.getColumnIndex("position"))
            res = Chat(id, name, avatar, news, position, nikkeids)
        }
        c.close()
        return res
    }

    @SuppressLint("Range")
    fun insertChat(chat: Chat): Int {
        val c = this.rd.rawQuery("select * from chat where position > ?", arrayOf(chat.position.toString()))
        var count = c.count
        return this.wd.insert(
            "chat",
            null,
            ContentValues().apply {
                put("name", chat.name)
                put("avatar", chat.avatar)
                put("news", chat.news)
                put("position", count + 1)
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
                put("position", chat.position)
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
