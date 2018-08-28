package com.mitash.moviepedia.view.interest

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemVerticalDiscoverBinding
import com.mitash.moviepedia.db.entity.InterestGenreRelation
import com.mitash.moviepedia.db.entity.Movie
import com.mitash.moviepedia.view.common.DataBoundListAdapter
import com.mitash.moviepedia.view.common.DataBoundViewHolder


/**
 * Created by Mitash Gaurh on 7/19/2018.
 */
class InterestVerticalAdapter(
        private val mAppExecutors: AppExecutors,
        private val callback: ((Movie) -> Unit)?
) : DataBoundListAdapter<InterestGenreRelation, ItemVerticalDiscoverBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<InterestGenreRelation>() {
            override fun areItemsTheSame(oldItem: InterestGenreRelation, newItem: InterestGenreRelation): Boolean {
                return oldItem.genre._id == newItem.genre._id
            }

            override fun areContentsTheSame(oldItem: InterestGenreRelation, newItem: InterestGenreRelation): Boolean {
                return oldItem.genre._id == newItem.genre._id
                        && oldItem.genre.name == newItem.genre.name
            }
        }
) {

    private val mViewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    private val mHorizontalScrollOffsetMap = HashMap<String, Int>()

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemVerticalDiscoverBinding {

        val binding: ItemVerticalDiscoverBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_vertical_discover,
                parent,
                false)

        val layoutManager = LinearLayoutManager(binding.root.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvHorizontalDiscover.layoutManager = layoutManager
        binding.rvHorizontalDiscover.recycledViewPool = mViewPool

        return binding
    }

    override fun bind(binding: ItemVerticalDiscoverBinding, item: InterestGenreRelation) {
        binding.header = item.genre.name

        val adapter = InterestHorizontalAdapter(mAppExecutors = mAppExecutors, callback = callback)
        binding.rvHorizontalDiscover.adapter = adapter

        if (item.interestMovies?.isNotEmpty()!!) {
            adapter.submitList(item.interestMovies)

            val horizontalScrollOffset = mHorizontalScrollOffsetMap[binding.header!!]
            if (null != horizontalScrollOffset && horizontalScrollOffset >= 0) {
                binding.rvHorizontalDiscover.scrollBy(horizontalScrollOffset, 0)
            }
        }
    }

    override fun onViewRecycled(holder: DataBoundViewHolder<ItemVerticalDiscoverBinding>) {
        mHorizontalScrollOffsetMap[holder.binding.header!!] = holder.binding.rvHorizontalDiscover.computeHorizontalScrollOffset()

        super.onViewRecycled(holder)
    }
}