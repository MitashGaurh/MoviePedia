package com.mitash.moviepedia.view.genre

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
 * Created by Mitash Gaurh on 7/24/2018.
 */
class GenreSelectAdapter(
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

    val mGenreSet: HashSet<String> = HashSet()

    override fun createBinding(parent: ViewGroup, viewType: Int): ItemGenreSelectBinding {
        val binding: ItemGenreSelectBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_genre_select,
                parent,
                false)

        binding.root.setOnClickListener {
            binding.genre?.isSelected = !binding.genre?.isSelected!!
            if (binding.genre?.isSelected!!) {
                mGenreSet.add(binding.genre?._id.toString())
            } else {
                mGenreSet.remove(binding.genre?._id.toString())
            }
            notifyDataSetChanged()
        }
        return binding
    }

    override fun bind(binding: ItemGenreSelectBinding, item: Genre) {
        binding.genre = item
    }
}