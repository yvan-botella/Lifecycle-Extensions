package com.wanwan.lifecycle_extension_demo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.wanwan.autolayout.AutoLayout
import com.wanwan.lifecycle_extension_demo.databinding.MainactivityBinding
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class MainActivity : BaseActivity<MainactivityBinding>() {

    //region Static Navigable
    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FirstFragment::class.java
    }
    //endregion Static


    //region Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.root?.setOnClickListener {
            MainActivity.navigate()
        }
    }

    override fun onResume() {
        super.onResume()
        title = javaClass.simpleName
    }

    //endregion Activity Lifecycle
}