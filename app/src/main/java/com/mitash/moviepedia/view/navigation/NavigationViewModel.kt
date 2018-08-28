package com.mitash.moviepedia.view.navigation

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.ArrayMap
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.util.ActivityUtils
import com.mitash.moviepedia.view.discover.DiscoverFragment
import com.mitash.moviepedia.view.interest.InterestFragment
import com.mitash.moviepedia.view.more.MoreFragment
import com.mitash.moviepedia.view.search.SearchFragment
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import com.mitash.moviepedia.vo.navigationstack.StackHolder
import com.mitash.moviepedia.vo.navigationstack.StackTransaction
import com.mitash.moviepedia.vo.navigationstack.Traverse
import java.util.*
import javax.inject.Inject


/**
 * Created by Mitash Gaurh on 7/20/2018.
 */
class NavigationViewModel @Inject constructor(
        private val mAppExecutors: AppExecutors
) : ViewModel() {

    companion object {
        private const val TAB_COUNT = 4
    }

    private var mFragmentManager: FragmentManager? = null

    private var mStackHolder: ArrayMap<Int, StackHolder> = ArrayMap(TAB_COUNT)

    private var mActiveFragment: Fragment? = null

    private var mCurrentTraverse: Traverse? = null

    private val mSelectDiscoverMenuEvent = SingleLiveEvent<Void>()

    fun initFragmentManager(fragmentManager: FragmentManager) {
        mFragmentManager = checkNotNull(fragmentManager)
        initStackHolder()
    }

    private fun initStackHolder() {
        mStackHolder[R.id.action_discover] = StackHolder(DiscoverFragment.newInstance())
        mStackHolder[R.id.action_search] = StackHolder(SearchFragment.newInstance())
        mStackHolder[R.id.action_interest] = StackHolder(InterestFragment.newInstance())
        mStackHolder[R.id.action_more] = StackHolder(MoreFragment.newInstance())
    }

    fun getSelectDiscoverMenuEvent(): SingleLiveEvent<Void> {
        return mSelectDiscoverMenuEvent
    }

    fun performBottomTabFragmentsTransaction(menuItemId: Int) {
        val traverse = mCurrentTraverse
        mCurrentTraverse = Traverse.values().find {
            it.value == menuItemId
        }

        if (traverse != mCurrentTraverse) {
            if (null != mStackHolder[menuItemId]?.tabFragment) {
                performStackOperation(mStackHolder[menuItemId]?.stack!!, mStackHolder[menuItemId]?.tabFragment!!
                        , true)
            }
        } else {
            if (mStackHolder[mCurrentTraverse?.value]?.stack?.size!! > 1) {
                multiBackStackOperation(mStackHolder[mCurrentTraverse?.value]?.stack!!)
            }
        }
    }

    fun isCurrentTraverseSameAsNavigationClick(menuItemId: Int): Boolean {
        val traverse = Traverse.values().find {
            it.value == menuItemId
        }
        return traverse == mCurrentTraverse
    }

    fun performStackFragmentsTransaction(stackTransaction: StackTransaction) {

        val stack: Stack<Fragment>? = mStackHolder[stackTransaction.traverse.value]?.stack

        mCurrentTraverse = stackTransaction.traverse

        performStackOperation(stack!!, stackTransaction.fragment, false)

    }

    private fun performStackOperation(stack: Stack<Fragment>, fragment: Fragment, isFromBottomTabs: Boolean) {
        if (mActiveFragment == fragment) {
            return
        }
        if (isFromBottomTabs) {
            if (stack.isEmpty()) {
                stack.push(fragment)
                performFragmentTransaction(stack.peek(), false)
            } else {
                performFragmentTransaction(stack.peek(), true)
            }
        } else {
            stack.push(fragment)
            performFragmentTransaction(stack.peek(), false)
        }
    }

    fun popBackStack(): Boolean {
        return if (mCurrentTraverse == Traverse.DISCOVER) {
            if (mStackHolder[Traverse.DISCOVER.value]?.stack?.size!! > 1) {
                backStackOperation(mStackHolder[Traverse.DISCOVER.value]?.stack!!)
                true
            } else {
                false
            }
        } else if (mCurrentTraverse == Traverse.SEARCH || mCurrentTraverse == Traverse.INTEREST || mCurrentTraverse == Traverse.MORE) {
            if (mStackHolder[mCurrentTraverse?.value]?.stack?.size!! > 1) {
                backStackOperation(mStackHolder[mCurrentTraverse?.value]?.stack!!)
            } else {
                returnToDiscoverTab(mCurrentTraverse!!)
            }
            true
        } else {
            false
        }
    }

    private fun returnToDiscoverTab(traverse: Traverse) {
        mAppExecutors.mainThread().execute {
            mFragmentManager?.beginTransaction()?.remove(mStackHolder[traverse.value]?.stack?.pop())?.commit()
        }
        mCurrentTraverse = Traverse.DISCOVER
        performFragmentTransaction(mStackHolder[Traverse.DISCOVER.value]?.stack?.peek()!!, true)
        mSelectDiscoverMenuEvent.call()
    }

    private fun multiBackStackOperation(stack: Stack<Fragment>) {
        while (stack.size > 1) {
            mFragmentManager?.beginTransaction()?.remove(stack.pop())?.commit()
        }
        performFragmentTransaction(stack.peek(), true)
    }

    private fun backStackOperation(stack: Stack<Fragment>) {
        mFragmentManager?.beginTransaction()?.remove(stack.pop())?.commit()
        performFragmentTransaction(stack.peek(), true)
    }

    private fun performFragmentTransaction(fragment: Fragment, isHideShow: Boolean) {
        ActivityUtils.addFragmentToContentContainer(this.mFragmentManager!!, fragment
                , mActiveFragment, R.id.content_container, fragment::class.simpleName!!, isHideShow)
        mActiveFragment = fragment

    }
}