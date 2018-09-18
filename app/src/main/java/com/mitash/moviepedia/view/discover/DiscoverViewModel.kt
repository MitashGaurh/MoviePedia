package com.mitash.moviepedia.view.discover

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.db.entity.DiscoverMovieRelation
import com.mitash.moviepedia.repository.DiscoverRepository
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
class DiscoverViewModel @Inject constructor(
        private val mDiscoverRepository: DiscoverRepository
        , private val mAppExecutors: AppExecutors) : ViewModel() {

    private val mBooleanLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val mDiscoverWithMoviesLiveData: LiveData<List<DiscoverMovieRelation>> = Transformations
            .switchMap(mBooleanLiveData) {
                mDiscoverRepository.loadDiscoverWithMovies()
            }

    fun setBooleanLiveData(value: Boolean) {
        mBooleanLiveData.postValue(value)
    }

    fun initiateFetchMoviesCall(context: Context, callback: (Boolean) -> Unit) {
        mAppExecutors.networkIO().execute {
            mDiscoverRepository.performNestedApiCall(context, false) {
                callback(it)
            }
        }
    }

    fun initiateGenreInterestCall(context: Context, callback: (Boolean) -> Unit) {
        mDiscoverRepository.discoverGenreInterestMovies(context) {
            if (it) {
                mDiscoverRepository.insertGenreInterestHeader { saved ->
                    if (saved) {
                        callback(true)
                    }
                }
            }
        }
    }

    fun nukeGenreInterestData() {
        mDiscoverRepository.deleteGenreInterestData()
    }
}