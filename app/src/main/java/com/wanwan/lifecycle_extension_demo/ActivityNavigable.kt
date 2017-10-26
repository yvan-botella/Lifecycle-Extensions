package com.wanwan.lifecycle_extension_demo

import android.databinding.ViewDataBinding
import com.wanwan.bindable.Bindable
import com.wanwan.lifecycle_callback.LifecycleActivity
import com.wanwan.lifecycle_callback.LifecycleAppCompatActivity
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
abstract class ActivityNavigable<T: ViewDataBinding>: LifecycleAppCompatActivity(), NavigableActivity, Bindable<T> {

    override var _binding: ViewDataBinding? = null

    override fun registerLifecycleCallback() {
        Bindable.register(this)
        NavigableActivity.register(this)
    }

    companion object: Navigable {
        override val activityAffinity = ActivityNavigable::class.java
        override val fragmentClass = FirstFragment::class.java
    }
}