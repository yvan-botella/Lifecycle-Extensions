package com.wanwan.lifecycle_extension_demo

import android.support.multidex.MultiDexApplication
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity
import com.wanwan.navigable.Navigable
import com.wanwan.navigable.ShareableElement

/**
 * Created by botella_y on 24/10/2017.
 */
class App: MultiDexApplication() {

    init {
        Navigable.init(this)

        NavigableActivity.navigableContainerId = R.id.container

        ShareableElement.sharedElementEnterTransitionId = 0
        ShareableElement.sharedElementReturnTransitionId = 0
    }
}