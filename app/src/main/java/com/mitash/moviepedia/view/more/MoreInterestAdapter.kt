package com.mitash.moviepedia.view.more

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemGenreSelectBinding
import com.mitash.moviepedia.db.entity.Genre
import com.mitash.moviepedia.view.common.DataBoundListAdapter

/**
 * Created by Mitash Gaurh on 8/22/2018.
 */
class MoreInterestAdapter(
        mAppExecutors: AppExecutors
) : DataBoundListAdapter<Genre, ItemGenreSelectBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem._id == newItem._id
                        && oldItem.name == newItem.name
            }
        }
) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemGenreSelectBinding {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_genre_select,
                parent,
                false)
    }

    override fun bind(binding: ItemGenreSelectBinding, item: Genre) {
        binding.genre = item
    }
}