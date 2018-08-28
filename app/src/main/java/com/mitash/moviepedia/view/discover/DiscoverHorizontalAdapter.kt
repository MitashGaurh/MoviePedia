package com.mitash.moviepedia.view.discover

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemHorizontalDiscoverBinding
import com.mitash.moviepedia.db.entity.Movie
import com.mitash.moviepedia.view.common.DataBoundListAdapter

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
class DiscoverHorizontalAdapter(
        mAppExecutors: AppExecutors,
        private val callback: ((Movie) -> Unit)?
) : DataBoundListAdapter<Movie, ItemHorizontalDiscoverBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem._id == newItem._id
                        && oldItem.overview == newItem.overview
            }
        }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemHorizontalDiscoverBinding {
        val binding = DataBindingUtil.inflate<ItemHorizontalDiscoverBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_horizontal_discover,
                parent,
                false)

        binding.root.setOnClickListener {
            binding.movie?.let { movie ->
                callback?.invoke(movie)
            }
        }
        return binding
    }

    override fun bind(binding: ItemHorizontalDiscoverBinding, item: Movie) {
        binding.movie = item
        binding.imageUrl = item.posterPath
    }

}