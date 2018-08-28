package com.mitash.moviepedia.util

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.*
import android.view.View
import android.support.v7.widget.StaggeredGridLayoutManager




/**
 * Created by Mitash Gaurh on 8/8/2018.
 */
open class ItemOffsetDecoration : RecyclerView.ItemDecoration {

    private var orientation = -1
    private var spanCount = -1
    private var spacing: Int = 0
    private var halfSpacing: Int = 0


    constructor(context: Context, @DimenRes spacingDimen: Int) {

        spacing = context.resources.getDimensionPixelSize(spacingDimen)
        halfSpacing = spacing / 2
    }

    constructor(spacingPx: Int) {

        spacing = spacingPx
        halfSpacing = spacing / 2
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (orientation == -1) {
            orientation = getOrientation(parent)
        }

        if (spanCount == -1) {
            spanCount = getTotalSpan(parent)
        }

        val childCount = parent.layoutManager.itemCount
        val childIndex = parent.getChildAdapterPosition(view)

        val itemSpanSize = getItemSpanSize(parent, childIndex)
        val spanIndex = getItemSpanIndex(view, parent, childIndex)

        /* INVALID SPAN */
        if (spanCount < 1) return

        setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex)
    }

    private fun setSpacings(outRect: Rect, parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int) {

        outRect.top = halfSpacing
        outRect.bottom = halfSpacing
        outRect.left = halfSpacing
        outRect.right = halfSpacing

        if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.top = spacing
        }

        if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.left = spacing
        }

        if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.right = spacing
        }

        if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.bottom = spacing
        }
    }

    private fun getTotalSpan(parent: RecyclerView): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanCount
        } else if (mgr is StaggeredGridLayoutManager) {
            return mgr.spanCount
        } else if (mgr is LinearLayoutManager) {
            return 1
        }

        return -1
    }

    private fun getItemSpanSize(parent: RecyclerView, childIndex: Int): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanSizeLookup.getSpanSize(childIndex)
        } else if (mgr is StaggeredGridLayoutManager) {
            return 1
        } else if (mgr is LinearLayoutManager) {
            return 1
        }

        return -1
    }

    private fun getItemSpanIndex(view: View, parent: RecyclerView, childIndex: Int): Int {
        val mgr = parent.layoutManager
        return when (mgr) {
            is GridLayoutManager -> mgr.spanSizeLookup.getSpanIndex(childIndex, spanCount)
            is StaggeredGridLayoutManager -> (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            is LinearLayoutManager -> 0
            else -> -1
        }

    }

    private fun getOrientation(parent: RecyclerView): Int {

        val mgr = parent.layoutManager
        return when (mgr) {
            is LinearLayoutManager -> mgr.orientation
            is GridLayoutManager -> mgr.orientation
            is StaggeredGridLayoutManager -> mgr.orientation
            else -> VERTICAL
        }

    }

    private fun isLeftEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {
        return if (orientation == VERTICAL) {
            spanIndex == 0
        } else {
            childIndex == 0 || isFirstItemEdgeValid(childIndex < spanCount, parent, childIndex)
        }
    }

    private fun isRightEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {
        return if (orientation == VERTICAL) {
            spanIndex + itemSpanSize == spanCount
        } else {
            isLastItemEdgeValid(childIndex >= childCount - spanCount, parent, childCount, childIndex, spanIndex)
        }
    }

    private fun isTopEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {
        return if (orientation == VERTICAL) {
            childIndex == 0 || isFirstItemEdgeValid(childIndex < spanCount, parent, childIndex)
        } else {
            spanIndex == 0
        }
    }

    private fun isBottomEdge(parent: RecyclerView, childCount: Int, childIndex: Int, itemSpanSize: Int, spanIndex: Int): Boolean {
        return if (orientation == VERTICAL) {
            isLastItemEdgeValid(childIndex >= childCount - spanCount, parent, childCount, childIndex, spanIndex)
        } else {
            spanIndex + itemSpanSize == spanCount
        }
    }

    private fun isFirstItemEdgeValid(isOneOfFirstItems: Boolean, parent: RecyclerView, childIndex: Int): Boolean {

        var totalSpanArea = 0
        if (isOneOfFirstItems) {
            for (i in childIndex downTo 0) {
                totalSpanArea += getItemSpanSize(parent, i)
            }
        }

        return isOneOfFirstItems && totalSpanArea <= spanCount
    }

    private fun isLastItemEdgeValid(isOneOfLastItems: Boolean, parent: RecyclerView, childCount: Int, childIndex: Int, spanIndex: Int): Boolean {

        var totalSpanRemaining = 0
        if (isOneOfLastItems) {
            for (i in childIndex until childCount) {
                totalSpanRemaining += getItemSpanSize(parent, i)
            }
        }

        return isOneOfLastItems && totalSpanRemaining <= spanCount - spanIndex
    }

    companion object {
        private const val VERTICAL = OrientationHelper.VERTICAL
    }
}