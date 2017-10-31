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


    //region Fragment Lifecycle
    /**
     * Called when View has been Bind, and ready to be used for setting up variables
     */
    override fun onBindDataContext() {
        binding?.context = this
    }
    //endregion Fragment Lifecycle


    //region Static Navigable
    companion object: Navigable {
        override val activityAffinity = MainActivity::class.java
        override val fragmentClass = FirstFragment::class.java

        /**
         * Navigate by pushing content
         */
        @JvmStatic
        fun navigateTo() {
            super.navigate(false, null)
        }

        /**
         * Navigate and clear backstack
         */
        @JvmStatic
        fun navigateFrom() {
            super.navigate(true, null)
        }
    }
    //endregion Static Navigable
}