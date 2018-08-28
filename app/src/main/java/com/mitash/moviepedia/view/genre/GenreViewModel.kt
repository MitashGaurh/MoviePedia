package com.mitash.moviepedia.view.genre

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.db.entity.Genre
import com.mitash.moviepedia.repository.InterestRepository
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 7/24/2018.
 */
class GenreViewModel @Inject constructor(mInterestRepository: InterestRepository) : ViewModel() {

    private val mBooleanLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val mGenreLiveData: LiveData<List<Genre>> = Transformations
            .switchMap(mBooleanLiveData) {
                mInterestRepository.loadGenres()
            }

    fun setBooleanLiveData(value: Boolean) {
        mBooleanLiveData.postValue(value)
    }
}