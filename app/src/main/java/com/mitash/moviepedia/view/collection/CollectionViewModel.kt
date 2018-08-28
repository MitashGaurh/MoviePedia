package com.mitash.moviepedia.view.collection

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.repository.CollectionRepository
import com.mitash.moviepedia.vo.apiresult.CollectionDetail
import com.mitash.moviepedia.vo.Resource
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
class CollectionViewModel @Inject constructor(private val mCollectionRepository: CollectionRepository) : ViewModel() {

    private val mBooleanLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    lateinit var mDetailLiveData: LiveData<Resource<CollectionDetail>>

    fun subscribeLiveData(collectionId: Int) {
        mDetailLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mCollectionRepository.collectionDetailsFetch(collectionId)
                }
    }

    fun setBooleanLiveData(value: Boolean) {
        mBooleanLiveData.postValue(value)
    }
}