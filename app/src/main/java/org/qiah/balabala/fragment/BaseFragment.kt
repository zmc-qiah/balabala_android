package org.qiah.balabala.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class BaseFragment<T : ViewBinding> : Fragment() {
    protected lateinit var find: T
        private set
    private var isLoaded = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        find = bindLayout()
//        initColorStatusBar(find.root)
        return find.root
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        lazyInit()
        // 从下一个activity回到此fragment只会触发onResume
        if (isCurrentFragment()) {
            onFragmentVisibilityChanged(true)
        }
    }

    override fun onResume() {
        super.onResume()
        lazyInit()

        // 从下一个activity回到此fragment只会触发onResume
        if (isCurrentFragment()) {
            onFragmentVisibilityChanged(true)
        }
    }

    private fun isCurrentFragment(): Boolean {
        // 单个fragment resume即认为可见
        if (activity != null && requireActivity().supportFragmentManager != null && requireActivity().supportFragmentManager.fragments != null) {
            if (requireActivity().supportFragmentManager.fragments.size == 1) {
                return true
            }
        }
        return !isHidden || isUserVisible
    }

    override fun onPause() {
        super.onPause()
        onFragmentVisibilityChanged(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    private fun lazyInit() {
        if (!isLoaded && !isHidden) {
            initView()
            subscribeUi()
            isLoaded = true
        }
    }

    private var mFragmentVisible = false
    private var isUserVisible = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUserVisible = isVisibleToUser
        onFragmentVisibilityChanged(isVisibleToUser)
    }

    protected open fun onFragmentVisibilityChanged(visible: Boolean) {
        if (!isAdded) {
            return
        }
        if (visible != mFragmentVisible) {
            if (visible) {
                onFragmentResume()
            } else {
                onFragmentPause()
            }
            mFragmentVisible = visible
        }
    }

    protected open fun onFragmentResume() {
    }

    protected open fun onFragmentPause() {
    }

    /** 绑定viewBinding  */
    protected abstract fun bindLayout(): T

    /** 初始化控件  */
    protected abstract fun initView()

    /** 订阅ViewModel  */
    protected abstract fun subscribeUi()
}
