package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.db.MoviePediaDb
import com.mitash.moviepedia.db.entity.Discover
import com.mitash.moviepedia.db.entity.DiscoverMovieRelation
import com.mitash.moviepedia.util.AppUtil
import com.mitash.moviepedia.util.DateUtil
import com.mitash.moviepedia.util.PreferenceUtil
import com.mitash.moviepedia.vo.AppConstants
import com.mitash.moviepedia.vo.apiresult.GenreObject
import com.mitash.moviepedia.vo.apiresult.Page
import com.mitash.moviepedia.vo.apiresult.RequestToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Singleton
class DiscoverRepository @Inject constructor(
        private val mAppExecutors: AppExecutors,
        private val mMoviePediaDb: MoviePediaDb,
        private val mMoviePediaService: MoviePediaService
) {
    companion object {
        private const val TAG = "DiscoverRepository"
    }

    fun executeGenreFetch(context: Context, isNestedCall: Boolean, callback: (Boolean) -> Unit) {
        mAppExecutors.networkIO().execute {
            val genreCall = mMoviePediaService.fetchGenres()

            genreCall.enqueue(object : Callback<GenreObject> {
                override fun onFailure(call: Call<GenreObject>?, t: Throwable?) {
                    callback(false)
                }

                override fun onResponse(call: Call<GenreObject>?, response: Response<GenreObject>?) {
                    mAppExecutors.diskIO().execute {
                        mMoviePediaDb.clearAllTables()
                        mMoviePediaDb.genreDao().insertGenres(response?.body()?.genres!!)
                        mAppExecutors.networkIO().execute {
                            if (isNestedCall) {
                                performNestedApiCall(context, callback)
                            } else {
                                callback(true)
                            }
                        }
                    }
                }

            })
        }
    }

    fun performNestedApiCall(context: Context, callback: (Boolean) -> Unit) {
        discoverPopularMovies { response ->
            if (response) {
                discoverBestMoviesFromYear { success ->
                    if (success) {
                        discoverTrendingMovies { response ->
                            if (response) {
                                discoverNowPlayingMovies { success ->
                                    if (success) {
                                        discoverTopRatedMovies { response ->
                                            if (response) {
                                                discoverUpcomingMovies { success ->
                                                    if (success) {
                                                        discoverGenreInterestMovies(context) {
                                                            insertDiscoverHeaders(context, callback)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun discoverPopularMovies(callback: (Boolean) -> Unit) {
        val trendingMoviesCall = mMoviePediaService.fetchPopularMovies()

        trendingMoviesCall.enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                callback(false)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_MOST_POPULAR, callback)
            }

        })
    }

    private fun discoverTopRatedMovies(callback: (Boolean) -> Unit) {
        val trendingMoviesCall = mMoviePediaService.fetchTopRatedMovies()

        trendingMoviesCall.enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                callback(false)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_TOP_RATED, callback)
            }

        })
    }

    private fun discoverBestMoviesFromYear(callback: (Boolean) -> Unit) {
        val trendingMoviesCall = mMoviePediaService.fetchBestMoviesFromYear(
                DateUtil.getPreviousYear(-10), MoviePediaService.SortBy.VOTE_AVERAGE_DESCENDING, 2000)

        trendingMoviesCall.enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                callback(false)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_BEST_FROM_YEAR, callback)
            }

        })
    }

    private fun discoverTrendingMovies(callback: (Boolean) -> Unit) {
        val trendingMoviesCall = mMoviePediaService.fetchTrendingMovies(MoviePediaService.SortBy.VOTE_COUNT_DESCENDING
                , DateUtil.getPreviousMonthDate(-6)
                , DateUtil.getPreviousMonthDate(0))

        trendingMoviesCall.enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                callback(false)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_TRENDING, callback)
            }
        })
    }

    private fun discoverNowPlayingMovies(callback: (Boolean) -> Unit) {
        val trendingMoviesCall = mMoviePediaService.fetchNowPlayingMovies()

        trendingMoviesCall.enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                callback(false)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_NOW_PLAYING, callback)
            }
        })
    }

    private fun discoverUpcomingMovies(callback: (Boolean) -> Unit) {
        val trendingMoviesCall = mMoviePediaService.fetchUpcomingMovies()

        trendingMoviesCall.enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                callback(false)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_UPCOMING, callback)
            }
        })
    }

    fun discoverGenreInterestMovies(context: Context, callback: (Boolean) -> Unit) {
        mAppExecutors.diskIO().execute {
            mMoviePediaDb.movieDao().deleteMovieByFetchType(AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES)
            mMoviePediaDb.discoverDao().deleteHeaderByFetchType(AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES)

            mAppExecutors.networkIO().execute {
                val selectedGenresMap = PreferenceUtil.getStringSetPreferences(context, AppConstants.SharedPrefConstants.SELECTED_GENRES)
                if (selectedGenresMap.isNotEmpty()) {
                    val selectedGenreStringBuffer = StringBuffer(selectedGenresMap.toString())
                    selectedGenreStringBuffer.deleteCharAt(0)
                    selectedGenreStringBuffer.deleteCharAt(selectedGenreStringBuffer.lastIndex)

                    val selectedGenreString = selectedGenreStringBuffer.toString().replace(", ", "|")

                    val trendingMoviesCall = mMoviePediaService.fetchGenreInterestMovies(selectedGenreString
                            , MoviePediaService.SortBy.VOTE_AVERAGE_DESCENDING, 2000)

                    trendingMoviesCall.enqueue(object : Callback<Page> {
                        override fun onFailure(call: Call<Page>?, t: Throwable?) {
                            callback(false)
                        }

                        override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                            insertMovies(response!!, AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES, callback)
                        }
                    })
                } else {
                    callback(false)
                }
            }
        }
    }

    fun deleteGenreInterestData() {
        mAppExecutors.diskIO().execute {
            mMoviePediaDb.movieDao().deleteMovieByFetchType(AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES)
            mMoviePediaDb.discoverDao().deleteHeaderByFetchType(AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES)
        }
    }

    private fun insertDiscoverHeaders(context: Context, callback: (Boolean) -> Unit) {
        mAppExecutors.diskIO().execute {
            mMoviePediaDb.discoverDao().insertHeaders(AppUtil.buildDiscoverHeaders(context))
            callback(true)
        }
    }

    fun insertGenreInterestHeader(callback: (Boolean) -> Unit) {
        mAppExecutors.diskIO().execute {
            val headerList = ArrayList<Discover>()
            headerList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES, "Top picks for you"))
            mMoviePediaDb.discoverDao().insertHeaders(headerList)
            callback(true)
        }
    }

    private fun insertMovies(response: Response<Page>, fetchType: String, callback: ((Boolean) -> Unit)?) {
        mAppExecutors.diskIO().execute {
            response.body()?.results?.forEach {
                it.fetchType = fetchType
            }
            response.body()?.results?.let { it ->
                mMoviePediaDb.movieDao().insertMovies(it)
            }
            callback?.invoke(true)
        }
    }

    fun loadDiscoverWithMovies(): LiveData<List<DiscoverMovieRelation>> {
        return mMoviePediaDb.discoverDao().loadDiscoverWithMovies()
    }

    fun obtainNewRequestToken(callback: (RequestToken) -> Unit) {
        val requestToken = mMoviePediaService.generateNewRequestToken()
        mAppExecutors.networkIO().execute {
            requestToken.enqueue(object : Callback<RequestToken> {
                override fun onFailure(call: Call<RequestToken>?, t: Throwable?) {
                    callback(RequestToken(false, null, null))
                }

                override fun onResponse(call: Call<RequestToken>?, response: Response<RequestToken>?) {
                    if (null != response) {
                        callback(response.body()!!)
                    }
                }
            })
        }
    }


    fun getReleaseDate(): String {
        val cal = Calendar.getInstance()

        val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        return format1.format(cal.time)
    }
}