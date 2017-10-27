package com.wanwan.bindable

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
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
import com.wanwan.lifecycle_callback.protocol.TAG

/**
 * Created by yvan.botella on 12/10/2017.
 */
interface Bindable<T: ViewDataBinding>: TAG {

    val layoutId: Int
    val binding: T?
        get() = _binding as? T

    var _binding: ViewDataBinding?

    fun onBindDataContext(): Unit?

    companion object : ActivityLifecycleRegister, FragmentLifecycleRegister {

        override fun register(activity: ActivityLifecycleImpl) {
            activity.registerActivityCallback(activityLifecycle)
        }

        override fun register(fragment: FragmentLifecycleImpl) {
            fragment.registerFragmentCallback(fragmentLifecycle)
        }

        private val activityLifecycle = object : ActivityLifecycleCallbacks {
            override fun onCreate(activity: Activity, savedInstanceState: Bundle?) {
                val viewRoot = activity.findViewById<ViewGroup>(android.R.id.content)

                if (activity is Bindable<*>) {
                    try {
                        activity._binding = DataBindingUtil.bind(viewRoot)
                    } catch (ex: IllegalArgumentException) {
                        try {
                            activity._binding = DataBindingUtil.bind(viewRoot.getChildAt(0))
                        } catch (ex: IllegalArgumentException) {
                            activity._binding = DataBindingUtil.setContentView(activity, activity.layoutId)
                        }
                    }
                    if (activity.binding != null) {
                        activity.onBindDataContext()
                    }
                }
            }
        }
        private val fragmentLifecycle = object : FragmentLifecycleCallbacks {
            override fun onCreateView(fragment: Any, backResult: View?, inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                if (fragment is Bindable<*>) {
                    if (backResult != null) {
                        try {
                            fragment._binding = DataBindingUtil.bind(backResult)
                        } catch (ex: IllegalArgumentException) {
                            fragment._binding = DataBindingUtil.inflate(inflater, fragment.layoutId, container, false)
                        }
                    } else {
                        fragment._binding = DataBindingUtil.inflate(inflater, fragment.layoutId, container, false)
                    }
                    if (fragment.binding != null) {
                        fragment.onBindDataContext()
                    }
                }
                return backResult
            }
        }
    }
}