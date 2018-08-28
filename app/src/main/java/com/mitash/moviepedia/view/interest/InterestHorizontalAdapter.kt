package com.mitash.moviepedia.view.interest

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemHorizontalDiscoverBinding
import com.mitash.moviepedia.db.entity.InterestMovieRelation
import com.mitash.moviepedia.db.entity.Movie
import com.mitash.moviepedia.view.common.DataBoundListAdapter

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
class InterestHorizontalAdapter(
        mAppExecutors: AppExecutors,
        private val callback: ((Movie) -> Unit)?
) : DataBoundListAdapter<InterestMovieRelation, ItemHorizontalDiscoverBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<InterestMovieRelation>() {
            override fun areItemsTheSame(oldItem: InterestMovieRelation, newItem: InterestMovieRelation): Boolean {
                return oldItem.movies!![0]._id == newItem.movies!![0]._id
            }

            override fun areContentsTheSame(oldItem: InterestMovieRelation, newItem: InterestMovieRelation): Boolean {
                return oldItem.movies!![0]._id == newItem.movies!![0]._id
                        && oldItem.movies!![0].title == newItem.movies!![0].title
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

    override fun bind(binding: ItemHorizontalDiscoverBinding, item: InterestMovieRelation) {
        if (item.movies != null && item.movies!!.isNotEmpty()) {
            binding.movie = item.movies!![0]
            binding.imageUrl = item.movies!![0].posterPath
        }
    }
}