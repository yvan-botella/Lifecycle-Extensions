package com.wanwan.lifecycle_extension_demo

import android.os.Bundle
import android.view.View
import com.wanwan.lifecycle_extension_demo.databinding.FirstfragmentBinding
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class FirstFragment : BaseFragment<FirstfragmentBinding>() {

    //region ShareableElement
    override val sharedElements: ArrayList<View?>
        get() = arrayListOf(binding?.title, binding?.layout, binding?.logo)
    //endregion ShareableElement

    val title = "FirstFragment"

    //region Fragment Lifecycle
    override fun onBindDataContext() {
        binding?.context = this
    }
    //endregion Fragment Lifecycle

    //region Static Navigable
    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FirstFragment::class.java

        @JvmStatic
        fun navigateTo() {
            super.navigate(false, null)
        }
        @JvmStatic
        fun navigateFrom() {
            super.navigate(true, null)
        }
    }
    //endregion Static Navigable
}