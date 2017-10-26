package com.wanwan.lifecycle_extension_demo

import android.support.multidex.MultiDexApplication
import com.wanwan.navigable.Navigable
import com.wanwan.navigable.ShareableElement

/**
 * Created by botella_y on 24/10/2017.
 */
class App: MultiDexApplication() {

    init {
        Navigable.init(this)
    }
}