package org.qiah.balabala.activity

import android.animation.ObjectAnimator
import android.graphics.Typeface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.adapter.MyFragmentStateAdapter
import org.qiah.balabala.bean.TabBean
import org.qiah.balabala.databinding.ItemMainTabBinding
import org.qiah.balabala.databinding.LayoutMainBinding
import org.qiah.balabala.fragment.MainFragment
import org.qiah.balabala.util.getHeight
import org.qiah.balabala.util.hide
import org.qiah.balabala.util.show

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
            val binding = ItemMainTabBinding.inflate(layoutInflater)
            tab.customView = binding.root
            tab.tag = binding
            binding.aTv.text = tabs.get(i).name
            if (i != 0) {
                binding.unLineView.hide()
            }
        }.attach()
        view.bgView.x
        view.tableLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    (it.tag as? ItemMainTabBinding)?.unLineView.show()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    (it.tag as? ItemMainTabBinding)?.unLineView.hide()
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    (it.tag as? ItemMainTabBinding)?.unLineView.show()
                }
            }
        })
        view.aTv.typeface = Typeface.createFromAsset(BaseApplication.context().getAssets(), "blabla.ttf")
    }
    companion object {
        @JvmStatic
        private var flag = false
    }
    override fun onStart() {
        super.onStart()
        if (!flag) {
            flag = true
            lifecycleScope.launch(Dispatchers.IO) {
                delay(600)
                launch(Dispatchers.Main) {
                    ObjectAnimator.ofFloat(
                        view.bgView,
                        "y",
                        0f,
                        -1.0f * getHeight()
                    ).apply {
                        duration = 400
                        start()
                    }
                }
            }
        }
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
