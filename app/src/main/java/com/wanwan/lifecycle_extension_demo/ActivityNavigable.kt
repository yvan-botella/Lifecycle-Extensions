package com.wanwan.lifecycle_extension_demo

import android.databinding.ViewDataBinding
import android.view.View
import com.wanwan.autolayout.AutoLayout
import com.wanwan.bindable.Bindable
import com.wanwan.lifecycle_callback.LifecycleActivity
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
open abstract class ActivityNavigable<T: ViewDataBinding>: LifecycleActivity(), NavigableActivity, Bindable<T> {

    override fun registerLifecycleCallback() {
        Bindable.register(this)
        NavigableActivity.register(this)
    }

    companion object: Navigable {
        override val activityAffinity = ActivityNavigable::class.java
        override val fragmentClass = FragmentNavigable::class.java
    }
}