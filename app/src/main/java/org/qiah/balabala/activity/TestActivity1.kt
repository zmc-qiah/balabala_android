package org.qiah.balabala.activity

import org.qiah.balabala.databinding.Activity1Binding

class TestActivity1 : BaseActivity<Activity1Binding>() {
    private val tag = "TestActivity1"
    override fun bindLayout(): Activity1Binding = Activity1Binding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun subscribeUi() {
    }

    override fun onStart() {
        super.onStart()
    }
}
