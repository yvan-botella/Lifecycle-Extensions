package com.wanwan.navigable

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentTransaction
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.View
import com.wanwan.lifecycle_callback.protocol.TAG
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by yvan.botella on 22/10/2017.
 */
class SharedElementTransaction: TAG {

    var token = UUID.randomUUID().toString()
    var sharedElements = ArrayList<Pair<View?, String>>()

    init {
        transactions.put(token, this)
    }

    fun toBundle(activity: Activity?): Bundle? {
        if (activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedElements.toTypedArray())

            val bundle = options.toBundle()
            bundle?.putString(SharedElementTransaction.TOKEN, token)

            return bundle
        }
        return null
    }

    fun setupTransaction(transaction: FragmentTransaction) {
        sharedElements.forEach { sharedElement->
            transaction.addSharedElement(sharedElement.first, sharedElement.second)
        }
        transactions.remove(token)
    }

    companion object: TAG {

        internal val TOKEN = SharedElementTransaction::class.java.name + ".TOKEN"
        val transactions = HashMap<String, SharedElementTransaction>()

        fun newTransaction(shareable: ShareableElement): SharedElementTransaction? {
            if (shareable.sharedElements.isNotEmpty()) {
                return SharedElementTransaction.newTransactionView(shareable.sharedElements)
            } else if (shareable.sharedElementsPair.isNotEmpty()) {
                return SharedElementTransaction.newTransactionPair(shareable.sharedElementsPair)
            } else {
                return null
            }
        }

        private fun newTransactionView(views: ArrayList<View?>): SharedElementTransaction {
            val transaction = SharedElementTransaction()

            views.forEach { view ->
                if (view != null) {
                    transaction.sharedElements.add(Pair(view, ViewCompat.getTransitionName(view)))
                }
            }

            return transaction
        }

        private fun newTransactionPair(pairs: ArrayList<Pair<View?, String>>): SharedElementTransaction {
            val transaction = SharedElementTransaction()
            pairs.forEach { pair ->
                if (pair.first != null) {
                    ViewCompat.setTransitionName(pair.first, pair.second)
                    transaction.sharedElements.add(pair)
                }
            }

            return transaction
        }

        fun fromBundle(bundle: Bundle?): SharedElementTransaction? {
            if (bundle != null && bundle.containsKey(TOKEN)) {
                val token = bundle.getString(TOKEN)

                transactions.keys.forEach { key ->
                    Log.d(TAG, key)
                }

                if (transactions.containsKey(token))
                    return transactions[token]
            }
            return null
        }

        fun fromIntent(intent: Intent?) = fromBundle(intent?.extras)
    }
}