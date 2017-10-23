package com.wanwan.lifecycle_extensions.kotlin

import android.view.View
import com.wanwan.lifecycle_callback.LifecycleActivity
import com.wanwan.lifecycle_callback.demo.protocol.Bindable
import com.wanwan.lifecycle_callback.demo.protocol.Navigable
import com.wanwan.lifecycle_extensions.R
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity

/**
 * Created by yvan.botella on 23/10/2017.
 */
class ActivityNavigable: LifecycleActivity(), NavigableActivity, Bindable<View> {

    override var binding: View? = null
    override val navigableContainerId = R.id.action_bar

    companion object: Navigable {
        override val activityAffinity = ActivityNavigable::class.java
        override val fragmentClass = FragmentNavigable::class.java
    }
}