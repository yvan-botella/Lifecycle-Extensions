package com.wanwan.lifecycle_extensions.kotlin.navigable

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.support.v4.app.FragmentActivity
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
            if (this is Activity) {
                return findViewById(_navigableContainerId)
            }
            return null
        }

    companion object : ActivityLifecycleRegister {

        override fun register(activity: ActivityLifecycleImpl) {
            activity.registerActivityCallback(lifecycle)
        }

        val lifecycle = object : ActivityLifecycleCallbacks {
            override fun onNewIntent(activity: Activity, intent: Intent?) {
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
            if (intent.hasExtra(Navigable.EXTRA_CLEAR_BACKSTACK)) {
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            var transaction = fragmentManager.beginTransaction()

            transaction = onNavigateTransaction(fragment, transaction) as FragmentTransaction

            transaction.commit()
        }
        else if (this is FragmentActivity && fragment is android.support.v4.app.Fragment) {
            if (intent.hasExtra(Navigable.EXTRA_CLEAR_BACKSTACK)) {
                supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            var transaction = supportFragmentManager.beginTransaction()

            transaction = onNavigateTransaction(fragment, transaction) as android.support.v4.app.FragmentTransaction

            if (this is ShareableElement) {
                SharedElementTransaction.fromIntent(intent)?.setupTransaction(transaction)
            }

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