package org.qiah.balabala.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.qiah.balabala.adapter.MyFragmentStateAdapter
import org.qiah.balabala.bean.TabBean
import org.qiah.balabala.databinding.LayoutMainBinding
import org.qiah.balabala.fragment.MainFragment

class MainActivity : BaseActivity<LayoutMainBinding>() {
    private var tabs = mutableListOf<TabBean>(
        TabBean("任务"),
        TabBean("妮姬"),
        TabBean("队伍")
    )
    private val adapter by lazy {
        setFragment()
        MyFragmentStateAdapter(fragments, this)
    }
    private lateinit var fragments: MutableList<Fragment>
    override fun initView() {
        view.viewPage.adapter = adapter
        view.viewPage.isUserInputEnabled = false
        TabLayoutMediator(view.tableLayout, view.viewPage) { tab, i ->
            tab.text = tabs.get(i).name
        }.attach()
    }
    override fun subscribeUi() {
    }
    private fun setFragment() {
        fragments = ArrayList<Fragment>()
        fragments.add(MainFragment())
        fragments.add(MainFragment())
        fragments.add(MainFragment())
    }
    override fun bindLayout(): LayoutMainBinding = LayoutMainBinding.inflate(layoutInflater)
}
