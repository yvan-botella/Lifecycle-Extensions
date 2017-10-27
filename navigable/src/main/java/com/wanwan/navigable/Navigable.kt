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
        val options = setupOptionsBundle()
        val extras = setupExtrasBundle()

        setupClearBackStack(clearBackStack, extras)

        startActivity(intent, extras, options)
        finishActivityIfNeeded(extras)
    }

    private fun startActivity(intent: Intent, extras: Bundle, options: Bundle?) {
        val previousActivity = currentActivity

        intent.putExtras(extras)
        if (previousActivity != null) {
            if (previousActivity.javaClass == activityAffinity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            previousActivity.startActivity(intent, options)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application?.startActivity(intent, extras)
        }
    }

    private fun finishActivityIfNeeded(extras: Bundle) {
        val previousActivity = currentActivity

        if (extras.containsKey(EXTRA_CLEAR_BACKSTACK)) {
            previousActivity?.setResult(Activity.RESULT_OK)

            if (previousActivity is FragmentActivity) {
                previousActivity.supportFinishAfterTransition()
            } else {
                previousActivity?.finish()
            }
        }
    }

    private fun setupClearBackStack(clearBackStack: Boolean, extras: Bundle) {
        if (clearBackStack) {
            extras.putBoolean(EXTRA_CLEAR_BACKSTACK, clearBackStack)
        }
    }

    private fun setupExtrasBundle(): Bundle {
        var extra = Bundle()
        extra.putSerializable(EXTRA_FRAGMENT_CLASS, fragmentClass)
        return extra
    }

    private fun setupOptionsBundle(): Bundle? {
        var optionBundle: Bundle? = null

        val previousActivity = currentActivity
        val previousFragment = (previousActivity as? NavigableActivity)?.currentFragment

        if (previousActivity is NavigableActivity && previousFragment is ShareableElement) {
            optionBundle = SharedElementTransaction.newTransaction(previousFragment)?.toBundle(previousActivity)
            if (optionBundle != null) {
                return optionBundle
            }
        }
        return null
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
                override fun onActivityResumed(activity: Activity?) {
                    currentActivity = activity
                }
                override fun onActivityStarted(activity: Activity?) {
                    currentActivity = activity
                }
                override fun onActivityDestroyed(activity: Activity?) {

                }
                override fun onActivitySaveInstanceState(activity: Activity?, savedInstanceState: Bundle?) {}
                override fun onActivityStopped(activity: Activity?) {}
                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    currentActivity = activity
                }
            })
        }
    }
}