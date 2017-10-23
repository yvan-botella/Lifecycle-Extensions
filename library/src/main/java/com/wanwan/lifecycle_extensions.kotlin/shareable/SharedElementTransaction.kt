package com.wanwan.sharedtransitiontest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentTransaction
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.view.View
import com.wanwan.lifecycle_callback.protocol.TAG
import java.util.*

/**
 * Created by yvan.botella on 22/10/2017.
 */
class SharedElementTransaction: TAG {

    var token: String
    var sharedElements: Array<Pair<View, String>?>

    init {
        token = UUID.randomUUID().toString()
    }

    private constructor(size: Int) {
        sharedElements = arrayOfNulls<Pair<View, String>>(size)
    }

    fun toBundle(activity: Activity?): Bundle {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedElements)

        val bundle = options.toBundle()
        bundle.putString(SharedElementTransaction.TOKEN, token)

        return bundle
    }

    fun setupTransaction(transaction: FragmentTransaction) {
        for (sharedElement in sharedElements) {
            transaction.addSharedElement(sharedElement?.first, sharedElement?.second)
        }
        transactions.remove(token)
    }

    companion object {

        internal val TOKEN = SharedElementTransaction::class.java!!.getName() + ".TOKEN"

        private val transactions = HashMap<String, SharedElementTransaction>()

        fun newTransaction(shareable: ShareableElement): SharedElementTransaction? {
            if (shareable.sharedElements != null) {
                return SharedElementTransaction.newTransaction(shareable.sharedElements!!)
            } else if (shareable.sharedElementsPair != null) {
                return SharedElementTransaction.newTransaction(shareable.sharedElementsPair!!)
            }
            return null
        }

        fun newTransaction(views: Array<View>): SharedElementTransaction {
            val transaction = SharedElementTransaction(views.size)

            var index = 0
            for (view in views) {
                transaction.sharedElements[index] = Pair(view, ViewCompat.getTransitionName(view))
                ++index
            }

            return transaction
        }

        fun newTransaction(pairs: Array<Pair<View, String>>): SharedElementTransaction {
            val transaction = SharedElementTransaction(pairs.size)

            var index = 0
            for (pair in pairs) {
                ViewCompat.setTransitionName(pair.first, pair.second)
                transaction.sharedElements[index] = Pair(pair.first, pair.second)
                ++index
            }

            return transaction
        }

        operator fun get(token: String): SharedElementTransaction? {
            return transactions[token]
        }

        fun fromBundle(bundle: Bundle?): SharedElementTransaction? {
            if (bundle != null && bundle.containsKey(TOKEN)) {
                val token = bundle.getString(TOKEN)
                return get(token)
            }

            return null
        }

        fun fromIntent(intent: Intent?): SharedElementTransaction? {
            if (intent != null) {
                return fromBundle(intent.extras)
            }

            return null
        }
    }
}