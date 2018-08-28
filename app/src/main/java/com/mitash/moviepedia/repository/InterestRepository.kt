package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.db.MoviePediaDb
import com.mitash.moviepedia.db.entity.Genre
import com.mitash.moviepedia.db.entity.Interest
import com.mitash.moviepedia.db.entity.InterestGenreRelation
import com.mitash.moviepedia.vo.AppConstants
import com.mitash.moviepedia.vo.apiresult.Page
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mitash Gaurh on 7/24/2018.
 */
@Singleton
class InterestRepository @Inject constructor(
        private val mAppExecutors: AppExecutors,
        private val mMoviePediaDb: MoviePediaDb,
        private val mMoviePediaService: MoviePediaService
) {
    companion object {
        private const val TAG = "InterestRepository"
    }

    fun loadGenres(): LiveData<List<Genre>> {
        return mMoviePediaDb.genreDao().loadGenreList()
    }

    fun loadMoviesWithGenres(genreIds: List<Int>): LiveData<List<InterestGenreRelation>> {
        return mMoviePediaDb.interestDao().loadMoviesBasedOnGenreIds(genreIds)
    }

    fun fetchMoviesBasedOnGenreIds(selectedGenreString: String, callback: ((Boolean) -> Unit)?) {
        mAppExecutors.diskIO().execute {
            mMoviePediaDb.movieDao().deleteMovieByFetchType(AppConstants.SetupConstants.FETCH_TYPE_INTEREST_MOVIES)
            mMoviePediaDb.interestDao().nukeTable()

            mAppExecutors.networkIO().execute {
                val selectedGenresList = selectedGenreString.split("|")
                selectedGenresList.forEach { selectedGenre ->
                    val trendingMoviesCall = mMoviePediaService.fetchGenreInterestMovies(selectedGenre
                            , MoviePediaService.SortBy.VOTE_AVERAGE_DESCENDING, 2000)

                    trendingMoviesCall.enqueue(object : Callback<Page> {
                        override fun onFailure(call: Call<Page>?, t: Throwable?) {
                            callback?.invoke(false)
                        }

                        override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                            insertMovies(response!!, selectedGenre, AppConstants.SetupConstants.FETCH_TYPE_INTEREST_MOVIES) {
                                if (it) {
                                    if (selectedGenresList.indexOf(selectedGenre) == selectedGenresList.size - 1) {
                                        callback?.invoke(true)
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    fun deleteInterestData() {
        mAppExecutors.diskIO().execute {
            mMoviePediaDb.movieDao().deleteMovieByFetchType(AppConstants.SetupConstants.FETCH_TYPE_INTEREST_MOVIES)
            mMoviePediaDb.interestDao().nukeTable()
        }
    }

    private fun insertMovies(response: Response<Page>, selectedGenre: String, fetchType: String, callback: ((Boolean) -> Unit)?) {
        mAppExecutors.diskIO().execute {
            response.body()?.results?.forEach {
                it.fetchType = fetchType
            }
            response.body()?.results?.let { it ->
                mMoviePediaDb.movieDao().insertMovies(it)
                val interests = ArrayList<Interest>()
                response.body()?.results?.forEach { movie ->
                    interests.add(Interest(movie._id, selectedGenre.toInt()))
                }
                mMoviePediaDb.interestDao().insertInterests(interests)
            }
            callback?.invoke(true)
        }
    }
}