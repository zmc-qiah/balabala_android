package org.qiah.balabala.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.R
import org.qiah.balabala.adapter.MultipleTypeAdapter
import org.qiah.balabala.bean.MainNikke
import org.qiah.balabala.databinding.FragmentNikkeBinding
import org.qiah.balabala.databinding.ItemNikkeMainBinding
import org.qiah.balabala.util.CommonItemDecoration
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
    override fun bindLayout(): FragmentNikkeBinding = FragmentNikkeBinding.inflate(layoutInflater)

    override fun initView() {
        find.chatRv.adapter = adapter
        find.chatRv.addItemDecoration(CommonItemDecoration(12F, CommonItemDecoration.VERTICAL))
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
