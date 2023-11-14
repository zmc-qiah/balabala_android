package org.qiah.balabala.activity

import org.qiah.balabala.databinding.LayoutMainBinding

class MainActivity : BaseActivity<LayoutMainBinding>() {

    override fun initView() {
    }
    override fun subscribeUi() {
        view.root.transitionToEnd()
    }
    override fun bindLayout(): LayoutMainBinding = LayoutMainBinding.inflate(layoutInflater)
}
