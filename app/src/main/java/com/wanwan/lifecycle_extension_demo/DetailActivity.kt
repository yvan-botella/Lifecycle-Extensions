package com.wanwan.lifecycle_extension_demo

import com.wanwan.lifecycle_extension_demo.databinding.DetailactivityBinding
import com.wanwan.navigable.Navigable

/**
 * Created by yvan.botella on 23/10/2017.
 */
class DetailActivity : BaseActivity<DetailactivityBinding>() {

    //region Static Navigable
    companion object: Navigable {
        override val activityAffinity = DetailActivity::class.java
        override val fragmentClass = SecondFragment::class.java
    }

    override fun onResume() {
        super.onResume()
        title = javaClass.simpleName
    }
    //endregion Static
}