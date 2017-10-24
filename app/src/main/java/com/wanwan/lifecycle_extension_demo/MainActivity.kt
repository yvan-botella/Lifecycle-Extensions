package com.wanwan.lifecycle_extension_demo

import com.keyrus.lifecycle_extension_demo.R
import com.keyrus.lifecycle_extension_demo.databinding.ActivityMainBinding
import com.wanwan.autolayout.AutoLayout
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class MainActivity : ActivityNavigable<ActivityMainBinding>(), AutoLayout {

    override var binding: ActivityMainBinding? = null
    override val navigableContainerId = 0
    override val layoutId = R.layout.activity_main

    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        super.registerLifecycleCallback()
    }

    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FragmentNavigable::class.java
    }
}