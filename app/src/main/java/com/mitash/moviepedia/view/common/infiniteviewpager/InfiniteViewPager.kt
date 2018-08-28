package com.mitash.moviepedia.view.common.infiniteviewpager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * A [ViewPager] that allows pseudo-infinite paging with a wrap-around effect. Should be used with an [ ].
 */
class InfiniteViewPager : ViewPager {

    private val offsetAmount: Int
        get() {
            if (adapter!!.count == 0) {
                return 0
            }
            return if (adapter is InfinitePagerAdapter) {
                val infAdapter = adapter as InfinitePagerAdapter?
                infAdapter!!.realCount * 100
            } else {
                0
            }
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        // offset first element so that we can scroll to the left
        currentItem = 0
    }

    override fun setCurrentItem(item: Int) {
        // offset the current item to ensure there is space to scroll
        setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        var tempItem = item
        if (adapter!!.count == 0) {
            super.setCurrentItem(tempItem, smoothScroll)
            return
        }
        tempItem = offsetAmount + tempItem % adapter!!.count
        super.setCurrentItem(tempItem, smoothScroll)
    }

    override fun getCurrentItem(): Int {
        if (adapter!!.count == 0) {
            return super.getCurrentItem()
        }
        val position = super.getCurrentItem()
        return if (adapter is InfinitePagerAdapter) {
            val infAdapter = adapter as InfinitePagerAdapter?
            // Return the actual item position in the data backing InfinitePagerAdapter
            position % infAdapter!!.realCount
        } else {
            super.getCurrentItem()
        }
    }
}