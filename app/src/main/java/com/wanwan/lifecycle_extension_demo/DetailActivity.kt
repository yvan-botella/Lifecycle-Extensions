package com.wanwan.lifecycle_extension_demo

import android.graphics.Color
import android.os.Bundle
import com.wanwan.autolayout.AutoLayout
import com.wanwan.lifecycle_extension_demo.databinding.DetailactivityBinding
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class DetailActivity : ActivityNavigable<DetailactivityBinding>(), AutoLayout {

    override val navigableContainerId = R.id.container
    override val layoutId: Int
        get() = super.layoutId

    override fun registerLifecycleCallback() {
        AutoLayout.register(this)
        super.registerLifecycleCallback()
    }

    override fun onResume() {
        super.onResume()
        title = TAG
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.root?.setBackgroundColor(Color.GREEN)
        binding?.root?.setOnClickListener { view ->
            MainActivity.navigate()
        }
    }
}