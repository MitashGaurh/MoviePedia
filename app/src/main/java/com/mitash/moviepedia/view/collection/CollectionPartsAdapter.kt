package com.mitash.moviepedia.view.collection

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ItemCollectionPartBinding
import com.mitash.moviepedia.view.common.DataBoundListAdapter
import com.mitash.moviepedia.vo.apiresult.CollectionDetail

/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
class CollectionPartsAdapter(
        mAppExecutors: AppExecutors,
        private val callback: ((CollectionDetail.Parts) -> Unit)?
) : DataBoundListAdapter<CollectionDetail.Parts, ItemCollectionPartBinding>(
        mAppExecutors = mAppExecutors,
        mDiffCallback = object : DiffUtil.ItemCallback<CollectionDetail.Parts>() {
            override fun areItemsTheSame(oldItem: CollectionDetail.Parts, newItem: CollectionDetail.Parts): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: CollectionDetail.Parts, newItem: CollectionDetail.Parts): Boolean {
                return oldItem._id == newItem._id
                        && oldItem.overview == newItem.overview
            }
        }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemCollectionPartBinding {
        val binding = DataBindingUtil.inflate<ItemCollectionPartBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_collection_part,
                parent,
                false)

        binding.root.setOnClickListener {
            binding.part?.let { part ->
                callback?.invoke(part)
            }
        }
        return binding
    }

    override fun bind(binding: ItemCollectionPartBinding, item: CollectionDetail.Parts) {
        binding.part = item
    }

}