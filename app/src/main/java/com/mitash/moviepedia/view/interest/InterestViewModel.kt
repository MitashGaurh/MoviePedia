package com.mitash.moviepedia.view.interest

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.db.entity.InterestGenreRelation
import com.mitash.moviepedia.repository.InterestRepository
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 8/21/2018.
 */
class InterestViewModel @Inject constructor(
        private val mInterestRepository: InterestRepository) : ViewModel() {
    private val mGenreIdsLiveData: SingleLiveEvent<List<Int>> = SingleLiveEvent()

    val mMoviesWithGenresLiveData: LiveData<List<InterestGenreRelation>> = Transformations
            .switchMap(mGenreIdsLiveData) {
                mInterestRepository.loadMoviesWithGenres(it)
            }

    fun setGenreIdsLiveData(value: List<Int>) {
        mGenreIdsLiveData.postValue(value)
    }

    fun initiateFetchInterestCall(selectedGenreString: String, callback: (Boolean) -> Unit) {
        mInterestRepository.fetchMoviesBasedOnGenreIds(selectedGenreString) {
            callback(it)
        }
    }

    fun nukeInterestData() {
        mInterestRepository.deleteInterestData()
    }
}
