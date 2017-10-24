package com.wanwan.navigable

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.util.Pair
import android.view.View
import com.wanwan.lifecycle_callback.ActivityLifecycleRegister
import com.wanwan.lifecycle_callback.callback.ActivityLifecycleCallbacks
import com.wanwan.lifecycle_callback.protocol.ActivityLifecycleImpl
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity

/**
 * Created by yvan.botella on 22/10/2017.
 */
interface ShareableElement {

    val sharedElements: Array<View>?
        get() = null
    val sharedElementsPair: Array<Pair<View, String>>?
        get() = null

    companion object {
        val EXTRA_TOKEN = "${ShareableElement::class.java.`package`}.intent.EXTRA_TOKEN"
    }
}