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
import java.util.regex.Pattern

/**
 * Created by yvan.botella on 12/10/2017.
 */
interface AutoLayout {

    val layoutId: Int
        get() = resolveLayout()

    private fun getLayoutName(): String {
        return javaClass.simpleName
                .swapLastWord()
                .camelToUnderscore()
    }

    private fun resolveLayout(): Int {
        val layoutName = getLayoutName()
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

    private fun String.swapLastWord(): String {
        val regex = "([a-z])([A-Z]+)"

        var pattern = Pattern.compile(regex)
        var matcher = pattern.matcher(this)
        var newString = this

        if(matcher.find()) {
            val lastWord = matcher.group(matcher.groupCount())
            newString = this.removeRange(this.length - lastWord.length, this.length)
            newString = lastWord + newString
        }

        return newString
    }

    private fun String.camelToUnderscore(): String {
        val regex = "([a-z])([A-Z]+)".toRegex()
        val replacement = "$1_$2";

        var layoutName = this
                .replace(regex, replacement)
                .toLowerCase()

        return layoutName
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