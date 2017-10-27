package com.wanwan.navigable

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.util.Pair
import android.transition.Transition
import android.view.View
import com.wanwan.lifecycle_callback.ActivityLifecycleRegister
import com.wanwan.lifecycle_callback.callback.ActivityLifecycleCallbacks
import com.wanwan.lifecycle_callback.protocol.ActivityLifecycleImpl
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity
import android.support.v4.app.ActivityCompat.startPostponedEnterTransition
import android.view.ViewTreeObserver



/**
 * Created by yvan.botella on 22/10/2017.
 */
interface ShareableElement {

    val sharedElements: ArrayList<View?>
        get() = ArrayList()
    val sharedElementsPair: ArrayList<Pair<View?, String>>
        get() = ArrayList()

    val hasAsyncSharedElement: Boolean
        get() = false

//    val enterTransitionId: Int
//        get() = ShareableElement.Companion.enterTransitionId
//    val exitTransitionId: Int
//        get() = ShareableElement.Companion.exitTransitionId

    val sharedElementEnterTransitionId: Int?
        get() = ShareableElement.Companion.sharedElementEnterTransitionId
    val sharedElementReturnTransitionId: Int?
        get() = ShareableElement.Companion.sharedElementReturnTransitionId

    companion object {
        var sharedElementEnterTransitionId: Int? = R.transition.default_shared_element_transition
        var sharedElementReturnTransitionId: Int? = R.transition.default_shared_element_transition
    }
}