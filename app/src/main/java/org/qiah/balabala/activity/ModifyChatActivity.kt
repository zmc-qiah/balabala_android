package org.qiah.balabala.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import org.qiah.balabala.MyListener.AddNikkeListener
import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.R
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.adapter.SingleTypeAdapter
import org.qiah.balabala.bean.ChatLeftImage
import org.qiah.balabala.bean.ChatLeftText
import org.qiah.balabala.bean.ChatRightImage
import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.bean.CreateChat
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.ActivityChatModifyBinding
import org.qiah.balabala.databinding.ItemChatLeftImageBinding
import org.qiah.balabala.databinding.ItemChatLeftTextBinding
import org.qiah.balabala.databinding.ItemChatRightImageBinding
import org.qiah.balabala.databinding.ItemChatRightTextBinding
import org.qiah.balabala.databinding.ItemEmojiBinding
import org.qiah.balabala.databinding.ItemNarrationBinding
import org.qiah.balabala.databinding.ItemSelectNikkeBinding
import org.qiah.balabala.databinding.ItemSplitLineBinding
import org.qiah.balabala.dialog.AddNikkeDialog
import org.qiah.balabala.util.GlideEngine
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.ResourceUtil
import org.qiah.balabala.util.SpanItemDecoration
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.hideKeyboard
import org.qiah.balabala.util.load
import org.qiah.balabala.util.show
import org.qiah.balabala.util.showKeyboard
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.util.toast
import org.qiah.balabala.viewHolder.ChatLeftImageViewHolder
import org.qiah.balabala.viewHolder.ChatLeftTextViewHolder
import org.qiah.balabala.viewHolder.ChatRightImageViewHolder
import org.qiah.balabala.viewHolder.ChatRightTextViewHolder
import org.qiah.balabala.viewHolder.MultipleViewHolder
import org.qiah.balabala.viewHolder.NarrationViewHolder
import org.qiah.balabala.viewHolder.NikkeAvatarViewHolder
import org.qiah.balabala.viewHolder.SingleViewHolder
import org.qiah.balabala.viewHolder.SplitViewHolder

class ModifyChatActivity : BaseActivity<ActivityChatModifyBinding>() {
    private val TAG = "ModifyChatA"
    private lateinit var nikkes: ArrayList<Nikke>
    private lateinit var selectNikke: Nikke
    private var selectType: Int = 1
    private val selectNikkeListener by lazy {
        object : SelectNikkeListener {
            override fun select(nikke: Nikke) {
                view.avatarIv.load(nikke.avatarPath, true)
                view.selectNikkeCL.gone()
                selectNikke = nikke
            }

            override fun unSelect(nikke: Nikke) {
                this.select(nikke)
            }
        }
    }
    private val adapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.CHAT_LEFT_TEXT -> ChatLeftTextViewHolder(ItemChatLeftTextBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_RIGHT_TEXT -> ChatRightTextViewHolder(ItemChatRightTextBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_LEFT_IMAGE -> ChatLeftImageViewHolder(ItemChatLeftImageBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_RIGHT_IMAGE -> ChatRightImageViewHolder(ItemChatRightImageBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_SPLIT_LINE -> SplitViewHolder(ItemSplitLineBinding.inflate(layoutInflater, viewGroup, false))
                MyType.CHAT_NARRATION -> NarrationViewHolder(ItemNarrationBinding.inflate(layoutInflater, viewGroup, false))
                else -> null
            }
        }
    }
    private val avatarAdapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = when (i) {
                MyType.NIKKE -> NikkeAvatarViewHolder(ItemSelectNikkeBinding.inflate(layoutInflater, viewGroup, false), selectNikkeListener, false)
                MyType.ADD_NIKKE -> object : MultipleViewHolder<ItemSelectNikkeBinding, MultipleType>(ItemSelectNikkeBinding.inflate(layoutInflater, viewGroup, false)) {
                    override fun setHolder(entity: MultipleType) {
                        view.avatarIv.load(ResourceUtil.getString(R.string.base_url) + "more.png")
                        view.root.singleClick {
                            AddNikkeDialog(object : AddNikkeListener {
                                override fun onClick(nikkes: ArrayList<Nikke>) {
                                    val adapter = bindingAdapter as MultipleTypeAdapter
                                    adapter.insert(adapter.itemCount - 1, nikkes)
                                }
                            }).show(supportFragmentManager)
                        }
                    }
                }
                else -> null
            }
        }
    }
    private val emojiAdapter by lazy {
        object : SingleTypeAdapter<String>() {
            override fun createViewHolder(
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = object : SingleViewHolder<ItemEmojiBinding, String>(ItemEmojiBinding.inflate(layoutInflater, viewGroup, false)) {
                override fun setHolder(entity: String) {
                    val width = getWidth() / 5
                    view.root.layoutParams.width = width
                    view.root.layoutParams.height = width
                    view.emojiIv.load(entity, 6)
                }
            }
        }
    }
    override fun bindLayout(): ActivityChatModifyBinding = ActivityChatModifyBinding.inflate(layoutInflater)

    override fun initView() {
        val nikkes = intent.getSerializableExtra("nikkes") as? CreateChat
        nikkes?.let {
            view.chatRv.adapter = adapter
            init(it)
            adapter.add(ChatLeftText("aaaaaa", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftText("aaaaaa"))
            adapter.add(ChatRightText("bbbbsajsaksjasa"))
            adapter.add(ChatRightImage("https://i0.hdslb.com/bfs/archive/c1804552a49963d9c5c6be8d4d285e4ce12ad8d7.jpg@472w_264h_1c_!web-dynamic.webp"))
            adapter.add(ChatLeftImage("https://album.biliimg.com/bfs/new_dyn/15293a912adc0f2d9ed60ffd4bfee754692529506.jpg@1048w_!web-dynamic.webp", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftImage("https://i0.hdslb.com/bfs/new_dyn/a86a7c0548d36d0a3b1fa5bf1a2ef64c336132667.png@1048w_!web-dynamic.webp"))
            adapter.add(ChatLeftText("aaaaaa", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftText("aaaaaa"))
            adapter.add(ChatRightText("bbbbsajsaksjasa"))
            adapter.add(ChatRightImage("https://i0.hdslb.com/bfs/archive/c1804552a49963d9c5c6be8d4d285e4ce12ad8d7.jpg@472w_264h_1c_!web-dynamic.webp"))
            adapter.add(ChatLeftImage("https://album.biliimg.com/bfs/new_dyn/15293a912adc0f2d9ed60ffd4bfee754692529506.jpg@1048w_!web-dynamic.webp", Nikke("红莲", "", R.drawable.chli)))
            adapter.add(ChatLeftImage("https://i0.hdslb.com/bfs/new_dyn/a86a7c0548d36d0a3b1fa5bf1a2ef64c336132667.png@1048w_!web-dynamic.webp"))
            loadClick()
        }
    }
    override fun subscribeUi() {
    }
    fun init(createChat: CreateChat) {
        view.inputRcl.transitionToState(R.id.end)
        nikkes = createChat.nikkes
        if (createChat.name.isNullOrEmpty()) {
            view.nameTv.text = nikkes.get(0).name
        } else {
            view.nameTv.text = createChat.name
            createChat.name.toast()
        }
        avatarAdapter.add(Nikke("指挥官", "其它", ResourceUtil.getString(R.string.base_url) + ResourceUtil.getString(R.string.zhi)).also { selectNikke = it })
        view.avatarIv.load(selectNikke, true)
        avatarAdapter.add(createChat.nikkes)
        avatarAdapter.add(object : MultipleType {
            override fun viewType(): Int = MyType.ADD_NIKKE
        })
        view.selectNikkeCL.adapter = avatarAdapter
        view.selectNikkeCL.addItemDecoration(SpanItemDecoration(4F, 8F, 2, true))
        view.emojiRv.adapter = emojiAdapter
    }
    fun loadClick() {
        view.selectNikkeCL.tag = false
        view.avatarIv.singleClick {
            val b = view.selectNikkeCL.tag as Boolean
            if (!b) {
                view.selectNikkeCL.show()
            } else {
                view.selectNikkeCL.gone()
            }
            view.selectNikkeCL.tag = !b
        }
        view.pictureIcon.tag = false
        view.pictureIcon.singleClick {
            val b = view.pictureIcon.tag as Boolean
            if (!b) {
                emojiAdapter.clear()
                emojiAdapter.add(ResourceUtil.getString(R.string.base_url) + "more.png")
                emojiAdapter.add(selectNikke.enoji)
                view.emojiRv.show()
                hideKeyboard(view.sendEt)
            } else {
                view.emojiRv.gone()
                showKeyboard(view.sendEt)
            }
            it.tag = !b
        }
//        view.backIcon.tag = false
        view.backIcon.singleClick {
            finish()
        }
        view.tpictureIcon.singleClick {
            PictureSelector.create(this)
                .openGallery(SelectMimeType.TYPE_IMAGE)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>?) {
                        result?.let {
                                it1 ->
                            val s = result.get(0).realPath
                            if ("指挥官".equals(selectNikke.name)) {
                                adapter.add(ChatRightImage(s))
                            } else {
                                adapter.add(ChatLeftImage(s, selectNikke))
                            }
                            view.chatRv.smoothScrollToPosition(adapter.itemCount - 1)
                            view.emojiRv.gone()
                        }
                    }
                    override fun onCancel() {
                    }
                })
        }
        view.sendEt.singleClick {
            view.emojiRv.gone()
        }
        view.tv1.tag = true
        view.tv2.tag = false
        view.tv3.tag = false
        view.tv1.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                view.tv2.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                view.tv3.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                it.background = ResourceUtil.getDrawable(R.drawable.bg_select_btn)
                it.setTextColor(ResourceUtil.getColor(R.color.common_text_black))
                view.tv2.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                view.tv3.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                selectType = 1
                it.tag = true
                view.tv2.tag = false
                view.tv3.tag = false
                view.pictureIcon.show()
                view.avatarIv.show()
                view.sendEt.setText("")
            }
        }
        view.tv2.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                view.emojiRv.gone()
                view.tv1.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                view.tv3.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                it.background = ResourceUtil.getDrawable(R.drawable.bg_select_btn)
                it.setTextColor(ResourceUtil.getColor(R.color.common_text_black))
                view.tv1.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                view.tv3.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                selectType = 2
                it.tag = true
                view.tv1.tag = false
                view.tv3.tag = false
                view.inputRcl.transitionToState(R.id.tureEnd)
            }
        }
        view.tv3.singleClick {
            val b = it.tag as Boolean
            if (!b) {
                view.emojiRv.gone()
                view.tv2.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                view.tv1.background = ResourceUtil.getDrawable(R.drawable.bg_unselect_btn)
                it.background = ResourceUtil.getDrawable(R.drawable.bg_select_btn)
                it.setTextColor(ResourceUtil.getColor(R.color.common_text_black))
                view.tv2.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                view.tv1.setTextColor(ResourceUtil.getColor(R.color.common_text_white))
                it.tag = true
                view.tv2.tag = false
                selectType = 3
                view.tv1.tag = false
                view.inputRcl.transitionToState(R.id.tureEnd)
            }
        }
        view.sendBtn.singleClick {
            var s: String
            if (!view.sendEt.text.toString().also { s= it }.isNullOrEmpty()) {
                when (selectType) {
                    1 -> {
                        if ("指挥官".equals(selectNikke.name)) {
                            adapter.add(ChatRightText(s))
                        } else {
                            adapter.add(ChatLeftText(s, selectNikke))
                        }
                    }
                    2 -> {
                        adapter.add(object : MyType(s) {
                            override fun viewType(): Int = MyType.CHAT_NARRATION
                        })
                        view.inputRcl.transitionToState(R.id.tureEnd)
                    }
                    3 -> {
                        adapter.add(object : MyType(s) {
                            override fun viewType(): Int = MyType.CHAT_SPLIT_LINE
                        })
                        view.inputRcl.transitionToState(R.id.tureEnd)
                    }
                }
                view.chatRv.smoothScrollToPosition(adapter.itemCount - 1)
                view.sendEt.setText("")
                hideKeyboard(view.sendEt)
                view.emojiRv.gone()
            }
        }
        view.sendEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null && !s.toString().isNullOrEmpty()) {
                    if (selectType == 1) view.inputRcl.transitionToState(R.id.start)
                } else {
                    if (selectType == 1) view.inputRcl.transitionToState(R.id.end)
                }
            }
        })
    }
}
