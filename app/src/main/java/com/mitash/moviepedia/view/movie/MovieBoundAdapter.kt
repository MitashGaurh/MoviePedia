package com.mitash.moviepedia.view.movie

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemMovieCastBinding
import com.mitash.moviepedia.databinding.ItemMovieVideoBinding
import com.mitash.moviepedia.db.entity.Cast
import com.mitash.moviepedia.db.entity.Video
import com.mitash.moviepedia.view.common.DataBoundListAdapter

/**
 * Created by Mitash Gaurh on 8/9/2018.
 */
class MovieBoundAdapter<T>(
        mAppExecutors: AppExecutors
) : DataBoundListAdapter<T, ViewDataBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return if (oldItem is Video && newItem is Video) {
                    oldItem._id == newItem._id
                } else if (oldItem is Cast && newItem is Cast) {
                    oldItem.castId == newItem.castId
                } else {
                    throw IllegalArgumentException("No such entity found.")
                }
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return if (oldItem is Video && newItem is Video) {
                    oldItem._id == newItem._id && oldItem.key == newItem.key
                } else if (oldItem is Cast && newItem is Cast) {
                    oldItem.castId == newItem.castId && oldItem.creditId == newItem.creditId
                } else {
                    throw IllegalArgumentException("No such entity found.")
                }
            }
        }
) {

    companion object {
        private const val TYPE_VIDEO = 101
        private const val TYPE_CAST = 102
    }

    private var mItemClickListener: ItemClickListener? = null

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        when (viewType) {
            TYPE_VIDEO -> {
                val binding: ItemMovieVideoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_movie_video, parent, false)

                binding.root.setOnClickListener {
                    binding.video.let { video ->
                        mItemClickListener?.onVideoItemClick(video!!)
                    }
                }
                return binding
            }

            TYPE_CAST -> {
                val binding: ItemMovieCastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_movie_cast, parent, false)

                binding.root.setOnClickListener {
                    binding.cast.let { cast ->
                        mItemClickListener?.onCastItemClick(cast!!)
                    }
                }
                return binding
            }

            else -> throw IllegalArgumentException("viewType not found.")
        }
    }

    override fun bind(binding: ViewDataBinding, item: T) {
        if (binding is ItemMovieVideoBinding) {
            binding.video = item as Video
        } else if (binding is ItemMovieCastBinding) {
            binding.cast = item as Cast
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Video) {
            TYPE_VIDEO
        } else {
            TYPE_CAST
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onVideoItemClick(item: Video)

        fun onCastItemClick(item: Cast)
    }
}