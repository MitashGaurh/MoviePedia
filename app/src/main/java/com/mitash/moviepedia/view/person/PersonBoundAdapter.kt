package com.mitash.moviepedia.view.person

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemMovieCastBinding
import com.mitash.moviepedia.databinding.ItemPersonImageBinding
import com.mitash.moviepedia.view.common.DataBoundListAdapter
import com.mitash.moviepedia.vo.apiresult.PersonCredits
import com.mitash.moviepedia.vo.apiresult.PersonImage

/**
 * Created by Mitash Gaurh on 8/17/2018.
 */
class PersonBoundAdapter<T>(
        mAppExecutors: AppExecutors,
        private val callback: ((PersonCredits.Cast) -> Unit)?
) : DataBoundListAdapter<T, ViewDataBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return if (oldItem is PersonImage.Results && newItem is PersonImage.Results) {
                    oldItem.filePath == newItem.filePath
                } else if (oldItem is PersonCredits.Cast && newItem is PersonCredits.Cast) {
                    oldItem._id == newItem._id
                } else {
                    throw IllegalArgumentException("No such entity found.")
                }
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return if (oldItem is PersonImage.Results && newItem is PersonImage.Results) {
                    oldItem.filePath == newItem.filePath
                } else if (oldItem is PersonCredits.Cast && newItem is PersonCredits.Cast) {
                    oldItem._id == newItem._id && oldItem.posterPath == newItem.posterPath
                } else {
                    throw IllegalArgumentException("No such entity found.")
                }
            }
        }
) {

    companion object {
        private const val TYPE_IMAGE = 101
        private const val TYPE_CREDIT = 102
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        when (viewType) {
            TYPE_IMAGE ->
                return DataBindingUtil.inflate<ItemPersonImageBinding>(LayoutInflater.from(parent.context),
                        R.layout.item_person_image, parent, false)

            TYPE_CREDIT -> {
                val binding: ItemMovieCastBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_movie_cast, parent, false)

                binding.root.setOnClickListener {
                    binding.credit.let { credit ->
                        callback?.invoke(credit!!)
                    }
                }
                return binding
            }

            else -> throw IllegalArgumentException("viewType not found.")
        }
    }

    override fun bind(binding: ViewDataBinding, item: T) {
        if (binding is ItemPersonImageBinding) {
            binding.result = item as PersonImage.Results
        } else if (binding is ItemMovieCastBinding) {
            binding.credit = item as PersonCredits.Cast
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is PersonImage.Results) {
            TYPE_IMAGE
        } else {
            TYPE_CREDIT
        }
    }
}