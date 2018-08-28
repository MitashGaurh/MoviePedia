package com.mitash.moviepedia.view.more

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.db.entity.Genre
import com.mitash.moviepedia.repository.MoreRepository
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 8/22/2018.
 */
class MoreViewModel @Inject constructor(mMoreRepository: MoreRepository) : ViewModel() {

    private val mGenreIdsLiveData: SingleLiveEvent<List<Int>> = SingleLiveEvent()

    val mSelectedGenresLiveData: LiveData<List<Genre>> = Transformations
            .switchMap(mGenreIdsLiveData) {
                mMoreRepository.loadSelectedGenres(it)
            }

    fun setGenreIdsLiveData(value: List<Int>) {
        mGenreIdsLiveData.postValue(value)
    }
}