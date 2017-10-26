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
import com.wanwan.lifecycle_extension_demo.databinding.SecondfragmentBinding
import com.wanwan.navigable.Navigable
import com.wanwan.navigable.R
import com.wanwan.navigable.ShareableElement

/**
 * Created by yvan.botella on 23/10/2017.
 */
class SecondFragment : LifecycleFragmentSupport(), AutoLayout, ShareableElement, Bindable<SecondfragmentBinding> {

    override var _binding: ViewDataBinding? = null

    override val sharedElements: ArrayList<View?>
        get() = arrayListOf(binding?.title, binding?.layout, binding?.logo)

    override val layoutId: Int
        get() = super.layoutId


    companion object: Navigable {
        override val activityAffinity = DetailActivity::class.java
        override val fragmentClass = SecondFragment::class.java
    }

    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        Bindable.register(this)
    }
}