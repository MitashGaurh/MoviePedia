package com.mitash.moviepedia.view.search

import android.databinding.DataBindingUtil
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemHorizontalDiscoverBinding
import com.mitash.moviepedia.db.entity.Search
import com.mitash.moviepedia.view.common.DataBoundListAdapter

/**
 * Created by Mitash Gaurh on 7/26/2018.
 */
class SearchAdapter(
        mAppExecutors: AppExecutors,
        private val callback: ((Search) -> Unit)?
) : DataBoundListAdapter<Search, ItemHorizontalDiscoverBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem._id == newItem._id
                        && oldItem.mediaType == newItem.mediaType
            }
        }
) {

    private val mDisplayMetrics: DisplayMetrics = DisplayMetrics();

    constructor(appExecutors: AppExecutors, activity: FragmentActivity
                , callback: ((Search) -> Unit)?) : this(appExecutors, callback) {
        activity.windowManager.defaultDisplay.getMetrics(mDisplayMetrics)
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemHorizontalDiscoverBinding {
        val binding = DataBindingUtil.inflate<ItemHorizontalDiscoverBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_horizontal_discover,
                parent,
                false)

        setDynamicHeight(binding)

        binding.root.setOnClickListener {
            binding.search?.let { search ->
                callback?.invoke(search)
            }
        }
        return binding
    }

    override fun bind(binding: ItemHorizontalDiscoverBinding, item: Search) {
        binding.search = item
        when {
            null != item.posterPath -> binding.imageUrl = item.posterPath
            null != item.profilePath -> binding.imageUrl = item.profilePath
            else -> binding.imageUrl = null
        }
    }

    private fun setDynamicHeight(binding: ItemHorizontalDiscoverBinding) {
        //if you need three fix imageView in width
        val deviceWidth = mDisplayMetrics.widthPixels / 3

        //if you need 4-5-6 anything fix imageView in height
        val deviceHeight = mDisplayMetrics.heightPixels / 3.50

        binding.cvDiscover.layoutParams.width = deviceWidth
        binding.cvDiscover.layoutParams.height = deviceHeight.toInt()
    }
}