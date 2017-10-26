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
class FirstFragment : LifecycleFragmentSupport(), AutoLayout, ShareableElement, Bindable<FirstfragmentBinding> {

    override var _binding: ViewDataBinding? = null

    override val sharedElements: ArrayList<View?>
        get() = arrayListOf(binding?.title, binding?.layout, binding?.logo)

    override val layoutId: Int
        get() = super.layoutId

    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        Bindable.register(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.setOnClickListener { view ->
            SecondFragment.navigate()
        }
    }

    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FirstFragment::class.java
    }
}