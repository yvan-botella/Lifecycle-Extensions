package com.wanwan.lifecycle_extensions.kotlin.navigable

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
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
            if (this is FragmentActivity) {
                val fragment = supportFragmentManager.findFragmentById(_navigableContainerId)
                return fragment
            }
            else if (this is Activity) {
                val fragment = fragmentManager.findFragmentById(_navigableContainerId)
                return fragment
            } else {
                return null
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
                if (activity is FragmentActivity && activity.supportFragmentManager.backStackEntryCount <= 1) {
                    activity.supportFinishAfterTransition()
                }
                else if (activity.fragmentManager.backStackEntryCount <= 1){
                    activity.finish()
                } else {
                    return false
                }
                return true
            }

            fun onHandleNavigableIntent(activity: Activity, intent: Intent?) {
                if (intent?.hasExtra(Navigable.EXTRA_FRAGMENT_CLASS) == true) {
                    if (activity is NavigableActivity) {
                        val fragmentClass = intent.getSerializableExtra(Navigable.EXTRA_FRAGMENT_CLASS) as Class<*>
                        val fragment = fragmentClass.newInstance()

                        if (fragment is Fragment || fragment is android.support.v4.app.Fragment) {
                            activity.onNavigate(intent, fragment)
                        }
                    }
                }
            }
        }
    }

    fun onNavigate(intent: Intent, fragment: Any) {
        if (this is Activity && fragment is Fragment) {
            fragment.retainInstance = true
            if (intent.hasExtra(Navigable.EXTRA_CLEAR_BACKSTACK)) {
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            var transaction = fragmentManager.beginTransaction()
            transaction = onNavigateTransaction(fragment, transaction) as FragmentTransaction
            transaction.commit()
        }
        else if (this is FragmentActivity && fragment is android.support.v4.app.Fragment) {
            fragment.retainInstance = true
            if (intent.hasExtra(Navigable.EXTRA_CLEAR_BACKSTACK)) {
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val exitFragment = currentFragment
                val enterFragment = fragment

                if (exitFragment is Fragment) {
                    val sharedElementEnterTransition = TransitionInflater.from(this)
                            .inflateTransition((exitFragment as? ShareableElement)?.sharedElementEnterTransitionId
                                    ?: sharedElementEnterTransitionId ?: 0)
                    exitFragment.sharedElementEnterTransition = sharedElementEnterTransition
                    exitFragment.enterTransition = sharedElementEnterTransition

                    val sharedElementReturnTransition = TransitionInflater.from(this)
                            .inflateTransition((exitFragment as? ShareableElement)?.sharedElementReturnTransitionId
                                    ?: sharedElementReturnTransitionId ?: 0)
                    exitFragment.sharedElementReturnTransition = sharedElementReturnTransition
                    exitFragment.exitTransition = sharedElementEnterTransition
                }

                val sharedElementEnterTransition = TransitionInflater.from(this)
                        .inflateTransition((enterFragment as? ShareableElement)?.sharedElementEnterTransitionId
                                ?: sharedElementEnterTransitionId ?: 0)
                enterFragment.sharedElementEnterTransition = sharedElementEnterTransition
                enterFragment.enterTransition = sharedElementEnterTransition

                val sharedElementReturnTransition = TransitionInflater.from(this)
                        .inflateTransition((enterFragment as? ShareableElement)?.sharedElementReturnTransitionId
                                ?: sharedElementReturnTransitionId ?: 0)
                enterFragment.sharedElementReturnTransition = sharedElementReturnTransition
                enterFragment.exitTransition = sharedElementEnterTransition
            }

            var transaction = supportFragmentManager.beginTransaction()

            transaction = onNavigateTransaction(fragment, transaction) as android.support.v4.app.FragmentTransaction
            SharedElementTransaction.fromIntent(intent)?.setupTransaction(transaction)

            transaction.commit()
        } else {

        }


    }

    fun onNavigateTransaction(fragment: Any, fragmentTransaction: Any): Any {
        if (fragment is Fragment && fragmentTransaction is FragmentTransaction) {
            return fragmentTransaction.replace(_navigableContainerId, fragment).addToBackStack(null)
        } else if (fragment is android.support.v4.app.Fragment && fragmentTransaction is android.support.v4.app.FragmentTransaction) {
            return fragmentTransaction.replace(_navigableContainerId, fragment).addToBackStack(null)
        }
        return fragmentTransaction
    }
}