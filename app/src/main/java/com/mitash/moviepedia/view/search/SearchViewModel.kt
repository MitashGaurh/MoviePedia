package com.mitash.moviepedia.view.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.db.entity.Search
import com.mitash.moviepedia.repository.SearchRepository
import com.mitash.moviepedia.vo.Resource
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 7/26/2018.
 */
class SearchViewModel @Inject constructor(private val mSearchRepository: SearchRepository) : ViewModel() {

    private val mQuery: SingleLiveEvent<String> = SingleLiveEvent()

    val mSearchLiveData: LiveData<Resource<List<Search>>> = Transformations
            .switchMap(mQuery) {
                mSearchRepository.performMultiSearch(it)
            }

    fun setQuery(update: String) {
        if (mQuery.value == update) {
            return
        }
        mQuery.value = update
    }

    fun clearSearchTable(){
        mSearchRepository.nukeSearchTable()
    }
}