package com.wanwan.lifecycle_extension_demo

import android.view.View
import com.wanwan.lifecycle_extension_demo.databinding.SecondfragmentBinding
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class SecondFragment : BaseFragment<SecondfragmentBinding>() {

    //region ShareableElement
    override val sharedElements: ArrayList<View?>
        get() = arrayListOf(binding?.title, binding?.layout, binding?.logo)
    //endregion

    //region Fragment Lifecycle
    override fun onBindDataContext() {
        binding?.context = this
//        binding?.setViewModel(ViewModelLocator.of(MainViewModel::class.java))
    }
    //endregion Fragment Lifecycle

    //region Static Navigable
    companion object: Navigable {
        override val activityAffinity = DetailActivity::class.java
        override val fragmentClass = SecondFragment::class.java

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