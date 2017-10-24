package com.keyrus.lifecycle_extension_demo

import com.keyrus.lifecycle_extension_demo.databinding.ActivityMainBinding
import com.wanwan.autolayout.AutoLayout
import com.wanwan.bindable.Bindable
import com.wanwan.lifecycle_callback.LifecycleActivity
import com.wanwan.lifecycle_callback.callback.ActivityLifecycleCallbacks
import com.wanwan.lifecycle_callback.protocol.ActivityLifecycleImpl
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class MainActivity : ActivityNavigable<ActivityMainBinding>(), AutoLayout {

    override var binding: ActivityMainBinding? = null
    override val navigableContainerId = R.id.action_bar
    override val layoutId = R.layout.activity_main

    override fun registerActivityCallback() {
        AutoLayout.register(this)
    }

    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FragmentNavigable::class.java
    }
}