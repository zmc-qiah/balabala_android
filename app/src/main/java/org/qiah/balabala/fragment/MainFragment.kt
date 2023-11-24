package org.qiah.balabala.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.MyListener.ClickCreateListener
import org.qiah.balabala.MyListener.ClickItemListener
import org.qiah.balabala.MyListener.ClickLongChatDialogListener
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.activity.ModifyChatActivity
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.bean.CreateChat
import org.qiah.balabala.databinding.FragmentNikkeBinding
import org.qiah.balabala.databinding.ItemNikkeMainBinding
import org.qiah.balabala.dialog.CreateChatDialog
import org.qiah.balabala.dialog.LongChatDialog
import org.qiah.balabala.util.CommonItemDecoration
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.dp
import org.qiah.balabala.util.getHeight
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
    private val chatAdapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.CHAT -> {
                    Log.d("chatAdapter", "createViewHolder: " + this.itemCount)
                    MainNikkeViewHolder(
                        ItemNikkeMainBinding.inflate(layoutInflater, viewGroup, false),
                        onClick,
                        longListener
                    )
                }
                else -> null
            }
        }
    }

    override fun bindLayout(): FragmentNikkeBinding = FragmentNikkeBinding.inflate(layoutInflater)

    override fun initView() {
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        find.root.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        find.sortIcon.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val i = (getHeight() * 0.81).toInt()
        val i1 = find.sortIcon.measuredHeight
        val i2: Float = 20.dp()
        Log.d("ChatFragment", "initView:${i}m${i1}m$i2 ")
        val params = find.chatRv.layoutParams
        params.height = i - i1 - i2.toInt()
        find.chatRv.layoutParams = params
        find.chatRv.adapter = chatAdapter
        find.chatRv.addItemDecoration(CommonItemDecoration(18F, CommonItemDecoration.VERTICAL))
        find.createBtn.singleClick {
            dialog.show(parentFragmentManager)
        }
        val chat = db.selectAllChat()
        find.numsTv.text = "对话清单:(${chat.size})"
        chatAdapter.add(chat.sortedBy { it.position })
    }

    override fun subscribeUi() {
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
                    chatAdapter.itemCount + 1,
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
    val longListener by lazy {
        object : LongItemListener {
            override fun onLongClick(location: IntArray, position: Int, entry: MyType) {
                LongChatDialog(location, position, entry as Chat, onClickDialog).show(parentFragmentManager, LongChatDialog::class.simpleName)
            }
        }
    }

    private val onClickDialog: ClickLongChatDialogListener by lazy {
        object : ClickLongChatDialogListener {
            override fun onMoveTo(entry: Chat, position: Int) {
                chatAdapter.moveToPostion(entry, 0)
                db.moveChatToTop(entry)
            }

            override fun onUpdate(entry: Chat, position: Int) {
            }

            override fun onDelete(entry: Chat, position: Int) {
                chatAdapter.remove(entry)
                db.deleteChat(entry)
                find.numsTv.text = "对话清单:(${chatAdapter.itemCount})"
            }
        }
    }
}
