package com.mitash.moviepedia.view.person

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.repository.PersonRepository
import com.mitash.moviepedia.vo.apiresult.PersonCredits
import com.mitash.moviepedia.vo.apiresult.PersonDetail
import com.mitash.moviepedia.vo.apiresult.PersonImage
import com.mitash.moviepedia.vo.Resource
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 8/16/2018.
 */
class PersonViewModel @Inject constructor(private val mPersonRepository: PersonRepository) : ViewModel() {

    private val mBooleanLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    lateinit var mDetailLiveData: LiveData<Resource<PersonDetail>>

    lateinit var mImagesLiveData: LiveData<Resource<PersonImage>>

    lateinit var mCreditsLiveData: LiveData<Resource<PersonCredits>>

    fun subscribeLiveData(personId: Int) {
        mDetailLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mPersonRepository.personDetailsFetch(personId)
                }

        mImagesLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mPersonRepository.personImagesFetch(personId)
                }

        mCreditsLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mPersonRepository.personCreditFetch(personId)
                }
    }

    fun setBooleanLiveData(value: Boolean) {
        mBooleanLiveData.postValue(value)
    }
}