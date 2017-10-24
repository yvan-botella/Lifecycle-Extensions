package com.wanwan.lifecycle_extension_demo

import android.view.View
import com.wanwan.lifecycle_callback.LifecycleFragment
import com.wanwan.navigable.Navigable
import com.wanwan.navigable.ShareableElement

/**
 * Created by yvan.botella on 23/10/2017.
 */
class FragmentNavigable: LifecycleFragment(), ShareableElement {

    override val sharedElements: Array<View>? = arrayOf(View(activity))

    companion object: Navigable {
        override val activityAffinity = ActivityNavigable::class.java
        override val fragmentClass = FragmentNavigable::class.java
    }
}