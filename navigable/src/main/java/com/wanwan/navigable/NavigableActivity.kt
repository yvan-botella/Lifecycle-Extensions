package com.wanwan.lifecycle_extensions.kotlin.navigable

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.transition.Transition
import android.transition.TransitionInflater
import com.wanwan.lifecycle_callback.ActivityLifecycleRegister
import com.wanwan.lifecycle_callback.callback.ActivityLifecycleCallbacks
import com.wanwan.lifecycle_callback.protocol.ActivityLifecycleImpl
import com.wanwan.lifecycle_callback.protocol.TAG
import com.wanwan.navigable.Navigable
import com.wanwan.navigable.ShareableElement
import com.wanwan.navigable.SharedElementTransaction

/**
 * Created by yvan.botella on 12/10/2017.
 */
interface NavigableActivity: TAG, ShareableElement {

    val navigableContainerId: Int

    private val _navigableContainerId: Int
        get() = if (navigableContainerId == 0) android.R.id.content else navigableContainerId

    val currentFragment: Any?
        get() {
            when (this) {
                is FragmentActivity -> return supportFragmentManager.findFragmentById(_navigableContainerId)
                is Activity -> return fragmentManager.findFragmentById(_navigableContainerId)
                else -> return null
            }
        }

    companion object : ActivityLifecycleRegister {

        override fun register(activity: ActivityLifecycleImpl) {
            activity.registerActivityCallback(lifecycle)
        }

        val lifecycle = object : ActivityLifecycleCallbacks {
            override fun onCreate(activity: Activity, savedInstanceState: Bundle?) {
                if (activity.intent?.hasExtra(Navigable.EXTRA_FRAGMENT_CLASS) == true) {
                    onHandleNavigableIntent(activity, activity.intent)
                }
            }

            override fun onNewIntent(activity: Activity, intent: Intent?) {
                onHandleNavigableIntent(activity, intent)
            }

            override fun onBackPressed(activity: Activity, handled: Boolean?): Boolean {
                when (activity) {
                    is FragmentActivity -> if (activity.supportFragmentManager.backStackEntryCount <= 1) {
                        activity.supportFinishAfterTransition()
                        return true
                    }
                    else -> if (activity.fragmentManager.backStackEntryCount <= 1) {
                        activity.finish()
                        return true
                    }
                }
                return false
            }

            fun onHandleNavigableIntent(activity: Activity, intent: Intent?) {
                if (intent?.hasExtra(Navigable.EXTRA_FRAGMENT_CLASS) == true) {
                    if (activity is NavigableActivity) {
                        val fragmentClass = intent.getSerializableExtra(Navigable.EXTRA_FRAGMENT_CLASS) as Class<*>
                        val fragment = fragmentClass.newInstance()

                        if (fragment is Fragment || fragment is android.support.v4.app.Fragment) {
                            if (fragment is ShareableElement && fragment.hasAsyncSharedElement) {
                                activity.postponeEnterTransition()
                            }
                            activity.onNavigate(intent, fragment)
                        }
                    }
                }
            }
        }
    }

    fun onNavigate(intent: Intent, fragment: Any) {
        when (this) {
            is FragmentActivity -> handleOnNavigateSupport(intent, this, fragment as? android.support.v4.app.Fragment)
            is Activity -> handleOnNavigate(intent, this, fragment as? Fragment)
        }
    }

    private fun handleOnNavigate(intent: Intent, activity: Activity, fragment: Fragment?) {
        if (intent.hasExtra(Navigable.EXTRA_CLEAR_BACKSTACK)) {
            activity.fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        var transaction = activity.fragmentManager.beginTransaction()
        transaction = onNavigateTransaction(fragment, transaction) as FragmentTransaction

        transaction.commit()
    }

    private fun handleOnNavigateSupport(intent: Intent, activity: FragmentActivity, fragment: android.support.v4.app.Fragment?) {
        if (intent.hasExtra(Navigable.EXTRA_CLEAR_BACKSTACK)) {
            activity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        if (currentFragment is android.support.v4.app.Fragment) {
            setFragmentSharedTransition(currentFragment as android.support.v4.app.Fragment, fragment)
        }

        var transaction = activity.supportFragmentManager.beginTransaction()

        transaction = onNavigateTransaction(fragment, transaction) as android.support.v4.app.FragmentTransaction
        SharedElementTransaction.fromIntent(intent)?.setupTransaction(transaction)

        transaction.commit()
    }


    fun setFragmentSharedTransition(enterFragment: android.support.v4.app.Fragment?, exitFragment: android.support.v4.app.Fragment?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val exitFragmentEnterTransition = inflateTransition((enterFragment as? ShareableElement)?.sharedElementEnterTransitionId, sharedElementEnterTransitionId)

            exitFragment?.sharedElementEnterTransition = exitFragmentEnterTransition
            exitFragment?.enterTransition = exitFragmentEnterTransition

            val exitFragmentReturnTransition = inflateTransition((enterFragment as? ShareableElement)?.sharedElementReturnTransitionId, sharedElementReturnTransitionId)
            exitFragment?.sharedElementReturnTransition = exitFragmentReturnTransition
            exitFragment?.exitTransition = exitFragmentReturnTransition

            val enterFragmentEnterTransition = inflateTransition((enterFragment as? ShareableElement)?.sharedElementEnterTransitionId, sharedElementEnterTransitionId)
            enterFragment?.sharedElementEnterTransition = enterFragmentEnterTransition
            enterFragment?.enterTransition = enterFragmentEnterTransition

            val enterFragmentReturnTransition = inflateTransition((enterFragment as? ShareableElement)?.sharedElementReturnTransitionId, sharedElementReturnTransitionId)
            enterFragment?.sharedElementReturnTransition = enterFragmentReturnTransition
            enterFragment?.exitTransition = enterFragmentReturnTransition
        }
    }

    fun inflateTransition(fragmentTransitionId: Int?, activityTransitionId: Int?): Transition {
        try {
            return TransitionInflater.from(this as Activity)
                    .inflateTransition(fragmentTransitionId ?: sharedElementReturnTransitionId ?: 0)
        } catch (ex: Resources.NotFoundException) {
            return TransitionInflater.from(this as Activity).inflateTransition(android.R.transition.no_transition)
        }
    }

    fun onNavigateTransaction(fragment: Any?, fragmentTransaction: Any): Any {
        if (fragment is Fragment && fragmentTransaction is FragmentTransaction) {
            return fragmentTransaction.replace(_navigableContainerId, fragment).addToBackStack(null)
        } else if (fragment is android.support.v4.app.Fragment && fragmentTransaction is android.support.v4.app.FragmentTransaction) {
            return fragmentTransaction.replace(_navigableContainerId, fragment).addToBackStack(null)
        }
        return fragmentTransaction
    }
}