package com.wanwan.lifecycle_extension_demo

import android.os.Bundle
import com.wanwan.autolayout.AutoLayout
import com.wanwan.lifecycle_extension_demo.databinding.MainactivityBinding
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class MainActivity : ActivityNavigable<MainactivityBinding>(), AutoLayout {

    //region Implement
    override val navigableContainerId = R.id.container
    override val layoutId: Int
        get() = super.layoutId
    //endregion Static

    //region Static
    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FirstFragment::class.java
    }
    //endregion Static


    override fun onBindDataContext() {
    }

    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        super.registerLifecycleCallback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.root?.setOnClickListener { view ->
            MainActivity.navigate()
        }
    }

    override fun onResume() {
        super.onResume()
        title = TAG
    }
}