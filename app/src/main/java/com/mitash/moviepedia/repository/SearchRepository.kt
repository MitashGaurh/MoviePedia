package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.db.dao.SearchDao
import com.mitash.moviepedia.db.entity.Search
import com.mitash.moviepedia.vo.Resource
import com.mitash.moviepedia.vo.apiresult.SearchPage
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 7/26/2018.
 */
class SearchRepository @Inject constructor(
        private val mAppExecutors: AppExecutors,
        private val mSearchDao: SearchDao,
        private val mMoviePediaService: MoviePediaService
) {
    fun performMultiSearch(query: String): LiveData<Resource<List<Search>>> {
        return object : NetworkBoundResource<List<Search>, SearchPage>(mAppExecutors) {
            override fun saveCallResult(item: SearchPage) {
                mSearchDao.nukeTable()
                mSearchDao.insertSearchResults(item.results)
            }

            override fun shouldFetch(data: List<Search>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Search>> {
                return mSearchDao.loadSearchResults()
            }

            override fun createCall() = mMoviePediaService.multiSearch(query)

            override fun onFetchFailed() {
                nukeSearchTable()
            }
        }.asLiveData()
    }

    fun nukeSearchTable() {
        mAppExecutors.diskIO().execute {
            mSearchDao.nukeTable()
        }
    }
}