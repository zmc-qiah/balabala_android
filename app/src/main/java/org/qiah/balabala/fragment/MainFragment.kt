package org.qiah.balabala.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.MyListener.ClickCreateListener
import org.qiah.balabala.R
import org.qiah.balabala.activity.ModifyChatActivity
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.CreateChat
import org.qiah.balabala.bean.MainNikke
import org.qiah.balabala.databinding.FragmentNikkeBinding
import org.qiah.balabala.databinding.ItemNikkeMainBinding
import org.qiah.balabala.dialog.CreateNikkeDialog
import org.qiah.balabala.util.CommonItemDecoration
import org.qiah.balabala.util.singleClick
import org.qiah.balabala.viewHolder.MainNikkeViewHolder

class MainFragment : BaseFragment<FragmentNikkeBinding>() {
    private val adapter by lazy {
        object : MultipleTypeAdapter() {
            override fun createViewHolder(
                i: Int,
                layoutInflater: LayoutInflater,
                viewGroup: ViewGroup
            ): RecyclerView.ViewHolder? = MainNikkeViewHolder(
                ItemNikkeMainBinding.inflate(layoutInflater, viewGroup, false)
            )
        }
    }
    private val listener: ClickCreateListener by lazy {
        object : ClickCreateListener {
            override fun onClick(nikkes: CreateChat) {
                val intent = Intent(requireActivity(), ModifyChatActivity::class.java)
                intent.putExtra("nikkes", nikkes)
                startActivity(intent)
            }
        }
    }
    private val dialog: CreateNikkeDialog
        get() {
            return CreateNikkeDialog(listener)
        }
    override fun bindLayout(): FragmentNikkeBinding = FragmentNikkeBinding.inflate(layoutInflater)

    override fun initView() {
        find.chatRv.adapter = adapter
        find.chatRv.addItemDecoration(CommonItemDecoration(12F, CommonItemDecoration.VERTICAL))
        find.createBtn.singleClick {
            dialog.show(parentFragmentManager)
        }
        adapter.add(
            BaseApplication.context().getDrawable(R.drawable.chli)?.let {
                MainNikke(
                    it,
                    "红莲",
                    "喝酒"
                )
            }
        )
        adapter.add(
            BaseApplication.context().getDrawable(R.drawable.chli)?.let {
                MainNikke(
                    it,
                    "红莲",
                    "喝酒"
                )
            }
        )
        adapter.add(
            BaseApplication.context().getDrawable(R.drawable.chli)?.let {
                MainNikke(
                    it,
                    "红莲",
                    "喝酒"
                )
            }
        )
    }

    override fun subscribeUi() {
    }
}
