package com.mitash.moviepedia.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Created by Mitash Gaurh on 7/20/2018.
 */
abstract class EndlessRecyclerOnScrollListener(
        private val mLayoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {
    private var mLoading = false // True if we are still waiting for the last set of data to load

    private var previousItemCount = 0 // The total number of items in the dataset after the last load

    private var mTotalEntries: Int = 0 // The total number of entries in the server
    private var mCurrentPage = 1 // Always start at Page 1

    // Concrete classes should implement the Loading of more data entries
    abstract fun onLoadMore(current_page: Int)

    fun setTotalEntries(totalEntries: Int) {
        mTotalEntries = totalEntries
    }

    // when you're RecyclerView supports refreshing, also refresh the count
    fun refresh() {
        mCurrentPage = 1
        previousItemCount = 0
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = mLayoutManager.itemCount
        var pastVisibleItems = -1
        var firstVisibleItem = -1

        if (mLayoutManager is LinearLayoutManager) {
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
        } else if (mLayoutManager is StaggeredGridLayoutManager) {
            var firstVisibleItems: IntArray? = null
            firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)
            if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                pastVisibleItems = firstVisibleItems[0]
            }
        }

        if (mLoading) {
            val diffCurrentFromPrevious = totalItemCount - previousItemCount

            // check if current total is greater than previous (diff should be greater than 1, for considering placeholder)
            // and if current total is equal to the total in server
            if (diffCurrentFromPrevious > 1 || totalItemCount >= mTotalEntries) {
                mLoading = false
                previousItemCount = totalItemCount
            }
        } else {

            if (totalItemCount > mTotalEntries) {
                // do nothing, we've reached the end of the list
            } else {
                // check if the we've reached the end of the list,
                // and if the total items is less than the total items in the server
                if (mLayoutManager is LinearLayoutManager) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount < mTotalEntries) {
                        onLoadMore(++mCurrentPage)

                        mLoading = true
                        previousItemCount = totalItemCount
                    }
                } else if (mLayoutManager is StaggeredGridLayoutManager) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        onLoadMore(++mCurrentPage)

                        mLoading = true
                        previousItemCount = totalItemCount
                    }
                }
            }
        }
    }

}