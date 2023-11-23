package org.qiah.balabala.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    lateinit var view: T
        private set
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = bindLayout()
        val actionBar = supportActionBar
        actionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        setContentView((view as ViewBinding?)!!.root)
        initView()
        subscribeUi()
    }
    abstract fun bindLayout(): T
    abstract fun initView()
    abstract fun subscribeUi()
}
