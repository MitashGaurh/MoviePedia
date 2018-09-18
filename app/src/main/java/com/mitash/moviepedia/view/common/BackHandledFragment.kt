package com.mitash.moviepedia.view.common

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by Mitash Gaurh on 4/20/2018.
 */
abstract class BackHandledFragment : Fragment() {

    companion object {
        private const val TAG = "BackHandledFragment"
    }

    private var mBackHandlerInterface: BackHandlerInterface? = null

    abstract fun onBackPressed(): Boolean

    open fun handleNavigationClick(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity !is BackHandlerInterface) {
            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            mBackHandlerInterface = activity as BackHandlerInterface
        }
    }

    override fun onStart() {
        super.onStart()

        // Mark this fragment as the selected Fragment.
        mBackHandlerInterface?.setSelectedFragment(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            // Mark this fragment as the selected Fragment.
            mBackHandlerInterface?.setSelectedFragment(this)
        }
    }

    fun onTraverseBack(): Boolean {
        return mBackHandlerInterface?.popBackStack()!!
    }

    fun onQueueTransaction(fragment: Fragment) {
        mBackHandlerInterface?.triggerStackTransaction(fragment)
    }

    interface BackHandlerInterface {

        fun setSelectedFragment(backHandledFragment: BackHandledFragment?)
        
        fun popBackStack(): Boolean

        fun triggerStackTransaction(fragment: Fragment)
    }
}
