package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.db.MoviePediaDb
import com.mitash.moviepedia.db.entity.Cast
import com.mitash.moviepedia.db.entity.Crew
import com.mitash.moviepedia.db.entity.Movie
import com.mitash.moviepedia.db.entity.Video
import com.mitash.moviepedia.vo.*
import com.mitash.moviepedia.vo.apiresult.CastPage
import com.mitash.moviepedia.vo.apiresult.MovieDetail
import com.mitash.moviepedia.vo.apiresult.Page
import com.mitash.moviepedia.vo.apiresult.VideoPage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mitash Gaurh on 8/8/2018.
 */
@Singleton
class MovieRepository @Inject constructor(
        private val mAppExecutors: AppExecutors,
        private val mMoviePediaDb: MoviePediaDb,
        private val mMoviePediaService: MoviePediaService
) {
    fun loadMovie(id: Int, fetchType: String): LiveData<Movie> {
        return mMoviePediaDb.movieDao().loadMovieByCriteria(id, fetchType)
    }

    fun performVideosFetch(movieId: Int): LiveData<Resource<List<Video>>> {
        return object : NetworkBoundResource<List<Video>, VideoPage>(mAppExecutors) {
            override fun saveCallResult(item: VideoPage) {
                item.results.forEach {
                    it.movieId = item.movieId
                }
                mMoviePediaDb.videoDao().insertVideos(item.results)
            }

            override fun shouldFetch(data: List<Video>?): Boolean {
                return null != data
            }

            override fun loadFromDb(): LiveData<List<Video>> {
                return mMoviePediaDb.videoDao().loadVideosByMovieId(movieId)
            }

            override fun createCall() = mMoviePediaService.fetchMovieVideos(movieId)

        }.asLiveData()
    }

    fun performDetailFetch(movieId: Int): LiveData<Resource<MovieDetail>> {
        val result = MediatorLiveData<Resource<MovieDetail>>()
        result.value = Resource.loading(null)
        mMoviePediaService.fetchMovieDetails(movieId).enqueue(object : Callback<MovieDetail> {
            override fun onFailure(call: Call<MovieDetail>?, t: Throwable?) {
                result.value = Resource.error(t?.message!!, null)
            }

            override fun onResponse(call: Call<MovieDetail>?, response: Response<MovieDetail>?) {
                result.value = Resource.success(response?.body())
            }
        })
        return result
    }

    fun performSimilarMoviesFetch(movieId: Int): LiveData<Resource<List<Movie>>> {
        val result = MediatorLiveData<Resource<List<Movie>>>()
        result.value = Resource.loading(null)
        mMoviePediaService.fetchSimilarMovie(movieId).enqueue(object : Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable?) {
                result.value = Resource.error(t?.message!!, null)
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>?) {
                result.value = Resource.success(response?.body()!!.results)
            }
        })
        return result
    }

    fun performCastsFetch(movieId: Int): LiveData<Resource<List<Cast>>> {
        return object : NetworkBoundResource<List<Cast>, CastPage>(mAppExecutors) {
            override fun saveCallResult(item: CastPage) {
                item.casts.forEach {
                    it.movieId = item.movieId
                }
                val crews = item.crews.filter {
                    (it.department == "Directing" && it.job == "Director") ||
                            (it.department == "Production" && it.job == "Executive Producer") ||
                            (it.department == "Writing" && it.job == "Screenplay")
                }
                crews.forEach {
                    it.movieId = item.movieId
                }
                mMoviePediaDb.castDao().insertCasts(item.casts)
                mMoviePediaDb.crewDao().insertCrews(crews)
            }

            override fun shouldFetch(data: List<Cast>?): Boolean {
                return null != data
            }

            override fun loadFromDb(): LiveData<List<Cast>> {
                return mMoviePediaDb.castDao().loadCastsByMovieId(movieId)
            }

            override fun createCall() = mMoviePediaService.fetchMovieCast(movieId)

        }.asLiveData()
    }

    fun loadMovieCrew(movieId: Int): LiveData<List<Crew>> {
        return mMoviePediaDb.crewDao().loadCrewsByMovieId(movieId)
    }
}