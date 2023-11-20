package org.qiah.balabala.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
import org.qiah.balabala.dialog.CreateNikkeDialog
import org.qiah.balabala.util.CommonItemDecoration
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.viewHolder.MainNikkeViewHolder

class MainFragment : BaseFragment<FragmentNikkeBinding>() {
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
    private val adapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = MainNikkeViewHolder(
                ItemNikkeMainBinding.inflate(layoutInflater, viewGroup, false),
                onClick
            )
        }
    }

    override fun bindLayout(): FragmentNikkeBinding = FragmentNikkeBinding.inflate(layoutInflater)

    override fun initView() {
        find.chatRv.adapter = adapter
        find.chatRv.addItemDecoration(CommonItemDecoration(18F, CommonItemDecoration.VERTICAL))
        find.createBtn.singleClick {
            dialog.show(parentFragmentManager)
        }
        val chat = db.selectAllChat()
        find.numsTv.text = "对话清单:(${chat.size})"
        adapter.add(chat)
    }

    override fun subscribeUi() {
    }

    override fun onResume() {
        selectChat?.let {
            db.selectChatById(it.id)?.let { new ->
                it.news = new.news
                selectPosition?.let {
                    if (it < adapter.itemCount && it >= 0) adapter.notifyItemChanged(it)
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
                Log.d("TAG", "selectAllexceptionChat:: " + chat.toString())
                chat.id = db.insertChat(chat)
                adapter.add(chat)
                find.numsTv.text = "对话清单:(${adapter.itemCount})"
                val intent = Intent(requireActivity(), ModifyChatActivity::class.java)
                intent.putExtra("chat", chat)
                startActivity(intent)
            }
        }
    }
    private val db by lazy {
        MyDataBaseHelper(requireContext(), "Nikke_balabala", 1)
    }

    private val dialog: CreateNikkeDialog
        get() {
            return CreateNikkeDialog(listener)
        }
}
