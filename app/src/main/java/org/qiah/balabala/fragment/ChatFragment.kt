package org.qiah.balabala.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.MyListener.ClickCreateListener
import org.qiah.balabala.MyListener.ClickItemListener
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.activity.ModifyChatActivity
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.bean.CreateChat
import org.qiah.balabala.databinding.FragmentNikkeBinding
import org.qiah.balabala.databinding.ItemNikkeMainBinding
import org.qiah.balabala.dialog.CreateChatDialog
import org.qiah.balabala.util.CommonItemDecoration
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getHeight
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.util.toast
import org.qiah.balabala.viewHolder.MainNikkeViewHolder

class ChatFragment : Fragment() {
    private lateinit var find: FragmentNikkeBinding
    private var selectChat: Chat? = null
    private var selectPosition: Int? = null
    private val onClick by lazy {
        object : ClickItemListener<Chat> {
            override fun onClick(data: Chat) {
            }

            override fun onClick(data: Chat, position: Int) {
                selectChat = data
                selectPosition = position
                val intent = Intent(requireActivity(), ModifyChatActivity::class.java)
                intent.putExtra("chat", data)
                startActivity(intent)
            }
        }
    }
    private val chatAdapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.CHAT -> MainNikkeViewHolder(
                    ItemNikkeMainBinding.inflate(layoutInflater, viewGroup, false),
                    onClick
                )
                else -> null
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        find = FragmentNikkeBinding.inflate(inflater)
        return find.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    fun initView() {
        val x1 = find.root.height
        val x2 = find.chatRv.x
        Log.d("ChatFragment", "initView: " + find.root.height + " " + find.chatRv.x + "" + getHeight())
        "bb".toast()
        find.chatRv.layoutParams.width = (x1 - x2).toInt()
        find.chatRv.adapter = chatAdapter
        find.chatRv.addItemDecoration(CommonItemDecoration(18F, CommonItemDecoration.VERTICAL))
        find.createBtn.singleClick {
            dialog.show(parentFragmentManager)
        }
        val chat = db.selectAllChat()
        find.numsTv.text = "对话清单:(${chat.size})"
        "${chat.size}".toast()
        chatAdapter.add(chat)
    }
    override fun onResume() {
        selectChat?.let {
            db.selectChatById(it.id)?.let { new ->
                it.news = new.news
                selectPosition?.let {
                    if (it < chatAdapter.itemCount && it >= 0) chatAdapter.notifyItemChanged(it)
                }
            }
        }
        super.onResume()
    }
    private val listener: ClickCreateListener by lazy {
        object : ClickCreateListener {
            override fun onClick(nikkes: CreateChat) {
                val chat = Chat(
                    1,
                    if (!nikkes.name.isNullOrEmpty()) nikkes.name else nikkes.nikkes.get(0).name,
                    nikkes.nikkes.get(0).avatarPath!!,
                    "",
                    ArrayList(nikkes.nikkes.map { it.id }),
                    nikkes.nikkes
                )
                chat.id = db.insertChat(chat)
                chatAdapter.add(chat)
                find.numsTv.text = "对话清单:(${chatAdapter.itemCount})"
                val intent = Intent(requireActivity(), ModifyChatActivity::class.java)
                intent.putExtra("chat", chat)
                startActivity(intent)
            }
        }
    }
    private val db by lazy {
        MyDataBaseHelper(requireContext(), "Nikke_balabala", 1)
    }

    private val dialog: CreateChatDialog
        get() {
            return CreateChatDialog(listener)
        }
}
