package org.qiah.balabala.activity

import org.qiah.balabala.databinding.Activity1Binding

class TestActivity2 : BaseActivity<Activity1Binding>() {
    override fun bindLayout(): Activity1Binding = Activity1Binding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun subscribeUi() {
    }
}
