package com.wanwan.lifecycle_extension_demo

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.util.Log
import com.wanwan.autolayout.AutoLayout
import com.wanwan.bindable.Bindable
import com.wanwan.lifecycle_callback.LifecycleAppCompatActivity
import com.wanwan.lifecycle_extension_demo.databinding.MainactivityBinding
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
abstract class BaseActivity<T: ViewDataBinding> :  LifecycleAppCompatActivity(), NavigableActivity, Bindable<T>, AutoLayout {

    override val navigableContainerId = R.id.container

    //region AutoLayout / Bindable
    override val layoutId: Int
        get() = super.layoutId
    //endregion AutoLayout / Bindable

    //region Bindable
    override var _binding: ViewDataBinding? = null
    //endregion Bindable

    //region Activity Lifecycle
    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        Bindable.register(this)
        NavigableActivity.register(this)
    }

    override fun onBindDataContext() {
        Log.i(TAG, "onBindDataContext")
    }
    //endregion Activity Lifecycle
}