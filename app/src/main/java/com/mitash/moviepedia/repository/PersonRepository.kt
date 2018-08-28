package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.vo.apiresult.PersonCredits
import com.mitash.moviepedia.vo.apiresult.PersonDetail
import com.mitash.moviepedia.vo.apiresult.PersonImage
import com.mitash.moviepedia.vo.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mitash Gaurh on 8/16/2018.
 */
@Singleton
class PersonRepository @Inject constructor(
        private val mMoviePediaService: MoviePediaService
) {
    fun personDetailsFetch(personId: Int): LiveData<Resource<PersonDetail>> {
        val result = MediatorLiveData<Resource<PersonDetail>>()
        result.value = Resource.loading(null)
        mMoviePediaService.fetchPersonDetails(personId).enqueue(object : Callback<PersonDetail> {
            override fun onFailure(call: Call<PersonDetail>?, t: Throwable?) {
                result.value = Resource.error(t?.message!!, null)
            }

            override fun onResponse(call: Call<PersonDetail>?, response: Response<PersonDetail>?) {
                result.value = Resource.success(response?.body())
            }
        })
        return result
    }

    fun personImagesFetch(personId: Int): LiveData<Resource<PersonImage>> {
        val result = MediatorLiveData<Resource<PersonImage>>()
        result.value = Resource.loading(null)
        mMoviePediaService.fetchPersonImages(personId).enqueue(object : Callback<PersonImage> {
            override fun onFailure(call: Call<PersonImage>?, t: Throwable?) {
                result.value = Resource.error(t?.message!!, null)
            }

            override fun onResponse(call: Call<PersonImage>?, response: Response<PersonImage>?) {
                result.value = Resource.success(response?.body())
            }
        })
        return result
    }

    fun personCreditFetch(personId: Int): LiveData<Resource<PersonCredits>> {
        val result = MediatorLiveData<Resource<PersonCredits>>()
        result.value = Resource.loading(null)
        mMoviePediaService.fetchPersonCredits(personId).enqueue(object : Callback<PersonCredits> {
            override fun onFailure(call: Call<PersonCredits>?, t: Throwable?) {
                result.value = Resource.error(t?.message!!, null)
            }

            override fun onResponse(call: Call<PersonCredits>?, response: Response<PersonCredits>?) {
                result.value = Resource.success(response?.body())
            }
        })
        return result
    }
}