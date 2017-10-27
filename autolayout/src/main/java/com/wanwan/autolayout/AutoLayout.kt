package com.wanwan.autolayout

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
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
        val layoutName = this::class.java.simpleName.toLowerCase()
        var ctx: Context? = null

        if (this is Activity) {
            ctx = this
        } else if (this is Fragment) {
            ctx = activity
        } else if (this is android.support.v4.app.Fragment) {
            ctx = activity
        } else if (this is View) {
            ctx = context
        }

        return ctx?.resources!!.getIdentifier(layoutName, "layout", ctx.packageName)
    }

    companion object: ActivityLifecycleRegister, FragmentLifecycleRegister {
        override fun register(activity: ActivityLifecycleImpl) {
            activity.registerActivityCallback(activityLifecycle)
        }

        override fun register(fragment: FragmentLifecycleImpl) {
            fragment.registerFragmentCallback(fragmentLifecycle)
        }

        private val fragmentLifecycle = object : FragmentLifecycleCallbacks {
            override fun onCreateView(fragment: Any, backResult: View?, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                if (fragment is AutoLayout) {
                    try {
                        return inflater?.inflate(fragment.layoutId, container, false)
                    } catch (ex: Resources.NotFoundException) {
                        throw Resources.NotFoundException("Layout ID: ${fragment.layoutId}")
                    }
                }
                return backResult
            }
        }

        private val activityLifecycle = object : ActivityLifecycleCallbacks {
            override fun onCreate(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is AutoLayout) {
                    try {
                        activity.setContentView(activity.layoutId)
                    } catch (ex: Resources.NotFoundException) {
                        throw Resources.NotFoundException("Layout ID: ${activity.layoutId}")
                    }

                }
            }
        }
    }
}