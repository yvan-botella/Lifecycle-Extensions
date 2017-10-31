package com.wanwan.lifecycle_extension_demo

import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import com.wanwan.autolayout.AutoLayout
import com.wanwan.bindable.Bindable
import com.wanwan.lifecycle_callback.LifecycleFragment
import com.wanwan.lifecycle_callback.kotlin.LifecycleFragmentSupport
import com.wanwan.lifecycle_extension_demo.databinding.FirstfragmentBinding
import com.wanwan.navigable.Navigable
import com.wanwan.navigable.R
import com.wanwan.navigable.ShareableElement

/**
 * Created by yvan.botella on 23/10/2017.
 */
abstract class BaseFragment<T: ViewDataBinding> : LifecycleFragmentSupport(), AutoLayout, ShareableElement, Bindable<T> {

    //region Bindable
    override var _binding: ViewDataBinding? = null
    //endregion Bindable

    //region AutoLayout / Bindable
    /**
     * layoutId used by AutoLayout / Bindable
     */
    override val layoutId: Int
        get() = super.layoutId
    //endregion AutoLayout

    //region Fragment Lifecycle
    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        Bindable.register(this)
    }
    //endregion Fragment Lifecycle
}