package com.wanwan.navigable

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.wanwan.lifecycle_callback.protocol.TAG
import com.wanwan.lifecycle_extensions.kotlin.navigable.NavigableActivity

/**
 * Created by yvan.botella on 12/10/2017.
 */
interface Navigable: TAG {

    val activityAffinity: Class<out Activity>
    val fragmentClass: Class<out Any>

    fun navigate(clearBackStack: Boolean = false, args: Bundle? = null) {
        val intent = Intent(currentActivity, activityAffinity)
        var bundle: Bundle? = null

        if (currentActivity is NavigableActivity && (currentActivity as NavigableActivity).currentFragment is ShareableElement) {
            bundle = SharedElementTransaction.newTransaction((currentActivity as NavigableActivity).currentFragment as ShareableElement)
                    ?.toBundle(currentActivity)
        }

        val previousActivity = currentActivity

        if (currentActivity != null && currentActivity?.isFinishing == false) {
            currentActivity?.startActivity(intent, bundle)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application?.startActivity(intent, bundle)
        }

        if (clearBackStack) {
            if (previousActivity is FragmentActivity) {
                previousActivity.supportFinishAfterTransition()
            } else {
                previousActivity?.finish()
            }
        }
    }


    companion object {
        val EXTRA_FRAGMENT_CLASS = "${Navigable::class.java.`package`}.intent.EXTRA_FRAGMENT_CLASS"
        val EXTRA_CLEAR_BACKSTACK = "${Navigable::class.java.`package`}.intent.EXTRA_CLEAR_BACKSTACK"

        private var application: Application? = null
        var currentActivity: Activity? = null
            private set

        fun init(application: Application) {
            this.application = application

            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity?) {}
                override fun onActivityResumed(activity: Activity?) {}
                override fun onActivityStarted(v: Activity?) {}
                override fun onActivityDestroyed(activity: Activity?) {}
                override fun onActivitySaveInstanceState(activity: Activity?, savedInstanceState: Bundle?) {}
                override fun onActivityStopped(activity: Activity?) {}
                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    currentActivity = activity
                }
            })
        }
    }
}