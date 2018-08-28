package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.vo.apiresult.CollectionDetail
import com.mitash.moviepedia.vo.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
@Singleton
class CollectionRepository @Inject constructor(
        private val mMoviePediaService: MoviePediaService
) {
    fun collectionDetailsFetch(collectionId: Int): LiveData<Resource<CollectionDetail>> {
        val result = MediatorLiveData<Resource<CollectionDetail>>()
        result.value = Resource.loading(null)
        mMoviePediaService.fetchCollectionDetails(collectionId).enqueue(object : Callback<CollectionDetail> {
            override fun onFailure(call: Call<CollectionDetail>?, t: Throwable?) {
                result.value = Resource.error(t?.message!!, null)
            }

            override fun onResponse(call: Call<CollectionDetail>?, response: Response<CollectionDetail>?) {
                result.value = Resource.success(response?.body())
            }
        })
        return result
    }
}