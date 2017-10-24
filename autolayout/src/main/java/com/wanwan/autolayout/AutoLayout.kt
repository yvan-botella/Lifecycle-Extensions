package com.wanwan.autolayout

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanwan.lifecycle_callback.ActivityLifecycleRegister
import com.wanwan.lifecycle_callback.FragmentLifecycleRegister
import com.wanwan.lifecycle_callback.callback.ActivityLifecycleCallbacks
import com.wanwan.lifecycle_callback.callback.FragmentLifecycleCallbacks
import com.wanwan.lifecycle_callback.protocol.ActivityLifecycleImpl
import com.wanwan.lifecycle_callback.protocol.FragmentLifecycleImpl

/**
 * Created by yvan.botella on 12/10/2017.
 */
interface AutoLayout {

    val layoutId: Int
        get() = resolveLayout()

    private fun resolveLayout(): Int {
        if (this is Activity) {
            val layoutName = this::class.java.simpleName.toLowerCase()
            return resources.getIdentifier(layoutName, "layout", packageName)
        }
        return 0
    }

    companion object: ActivityLifecycleRegister, FragmentLifecycleRegister {
        override fun register(activity: ActivityLifecycleImpl) {
            activity.registerActivityCallback(activityLifecycle)
        }

        override fun register(fragment: FragmentLifecycleImpl) {
            fragment.registerFragmentCallback(fragmentLifecycle)
        }

        private val fragmentLifecycle = object : FragmentLifecycleCallbacks {
            override fun onCreateView(fragment: Any, result: View?, inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                if (fragment is AutoLayout) {
                    return inflater?.inflate(fragment.layoutId, container, false)
                }
                return result
            }
        }

        private val activityLifecycle = object : ActivityLifecycleCallbacks {
            override fun onCreate(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is AutoLayout) {
                    activity.setContentView(activity.layoutId)
                }
            }
        }
    }
}