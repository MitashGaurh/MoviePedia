package com.mitash.moviepedia.view.discover

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemVerticalDiscoverBinding
import com.mitash.moviepedia.db.entity.DiscoverMovieRelation
import com.mitash.moviepedia.db.entity.Movie
import com.mitash.moviepedia.view.common.DataBoundListAdapter


/**
 * Created by Mitash Gaurh on 7/19/2018.
 */
class DiscoverVerticalAdapter(
        private val mAppExecutors: AppExecutors,
        private val callback: ((Movie) -> Unit)?
) : DataBoundListAdapter<DiscoverMovieRelation, ItemVerticalDiscoverBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<DiscoverMovieRelation>() {
            override fun areItemsTheSame(oldItem: DiscoverMovieRelation, newItem: DiscoverMovieRelation): Boolean {
                return oldItem.discover.fetchType == newItem.discover.fetchType
            }

            override fun areContentsTheSame(oldItem: DiscoverMovieRelation, newItem: DiscoverMovieRelation): Boolean {
                return oldItem.discover.fetchType == newItem.discover.fetchType
                        && oldItem.discover.header == newItem.discover.header
            }
        }
) {

    private val mViewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemVerticalDiscoverBinding {

        val binding: ItemVerticalDiscoverBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_vertical_discover,
                parent,
                false)

        binding.rvHorizontalDiscover.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(binding.root.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvHorizontalDiscover.layoutManager = layoutManager
        binding.rvHorizontalDiscover.recycledViewPool = mViewPool

        val adapter = DiscoverHorizontalAdapter(mAppExecutors = mAppExecutors, callback = callback)
        binding.rvHorizontalDiscover.adapter = adapter

        return binding
    }

    override fun bind(binding: ItemVerticalDiscoverBinding, item: DiscoverMovieRelation) {
        binding.header = item.discover.header

        (binding.rvHorizontalDiscover.adapter as DiscoverHorizontalAdapter).submitList(item.optionNameList)
    }
}