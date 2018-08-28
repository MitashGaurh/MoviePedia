package com.mitash.moviepedia.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.View


/**
 * This provides methods to help Activities load their UI.
 */
class ActivityUtils
/**
 * Don't let anyone instantiate this class.
 */
private constructor() {

    init {
        throw Error("Do not need instantiate!")
    }

    companion object {

        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addFragmentToActivity(fragmentManager: FragmentManager
                                  , fragment: Fragment, frameId: Int
                                  , isAddToBackStack: Boolean, tag: String?) {
            checkNotNull(fragmentManager)
            val transaction = fragmentManager.beginTransaction()
            if (null != tag) {
                transaction.replace(frameId, fragment, tag)
            } else {
                transaction.replace(frameId, fragment)
            }
            if (isAddToBackStack) {
                transaction.addToBackStack(fragment.javaClass.simpleName)
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }

        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addNonUIFragmentToActivity(fragmentManager: FragmentManager
                                       , nonUIFragment: Fragment, fragmentTag: String) {
            checkNotNull(fragmentManager)
            checkNotNull(nonUIFragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.add(nonUIFragment, fragmentTag)
            transaction.commit()
        }

        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addFragmentToActivityWithSharedElement(fragmentManager: FragmentManager
                                                   , fragment: Fragment, frameId: Int
                                                   , sharedElement: View, transitionName: String
                                                   , isAddToBackStack: Boolean, tag: String?) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.addSharedElement(sharedElement, transitionName)
            if (null != tag) {
                transaction.replace(frameId, fragment, tag)
            } else {
                transaction.replace(frameId, fragment)
            }
            if (isAddToBackStack) {
                transaction.addToBackStack(fragment.javaClass.simpleName)
            }
            transaction.commit()
        }

        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addFragmentToContentContainer(fragmentManager: FragmentManager, fragment: Fragment
                                          , activeFragment: Fragment?, frameId: Int
                                          , tag: String, hideShow: Boolean) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            if (hideShow) {
                if (fragmentManager.fragments.contains(activeFragment)) {
                    transaction.hide(activeFragment)
                }
                transaction.show(fragment)
            } else {
                if (null != activeFragment && fragmentManager.fragments.contains(activeFragment)) {
                    transaction.hide(activeFragment)
                }
                transaction.add(frameId, fragment, tag)
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }
    }
}
