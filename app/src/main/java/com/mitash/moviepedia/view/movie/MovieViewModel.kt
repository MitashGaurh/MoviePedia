package com.mitash.moviepedia.view.movie

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.mitash.moviepedia.db.entity.Cast
import com.mitash.moviepedia.db.entity.Crew
import com.mitash.moviepedia.db.entity.Movie
import com.mitash.moviepedia.db.entity.Video
import com.mitash.moviepedia.repository.MovieRepository
import com.mitash.moviepedia.vo.apiresult.MovieDetail
import com.mitash.moviepedia.vo.Resource
import com.mitash.moviepedia.vo.event.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 8/8/2018.
 */
class MovieViewModel @Inject constructor(private val mMovieRepository: MovieRepository) : ViewModel() {

    private val mBooleanLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    var mMovieLiveData: LiveData<Movie>? = null

    lateinit var mVideosLiveData: LiveData<Resource<List<Video>>>

    lateinit var mDetailLiveData: LiveData<Resource<MovieDetail>>

    lateinit var mSimilarMoviesLiveData: LiveData<Resource<List<Movie>>>

    lateinit var mCastsLiveData: LiveData<Resource<List<Cast>>>

    lateinit var mCrewLiveData: LiveData<List<Crew>>

    fun subscribeLocalMovieLiveData(movieId: Int, fetchType: String) {
        mMovieLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mMovieRepository.loadMovie(movieId, fetchType)
                }

        subscribeRemoteMovieLiveData(movieId)
    }

    fun subscribeRemoteMovieLiveData(movieId: Int) {
        mDetailLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mMovieRepository.performDetailFetch(movieId)
                }

        subscribeLiveData(movieId)
    }

    private fun subscribeLiveData(movieId: Int) {

        mVideosLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mMovieRepository.performVideosFetch(movieId)
                }

        mCastsLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mMovieRepository.performCastsFetch(movieId)
                }

        mCrewLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mMovieRepository.loadMovieCrew(movieId)
                }

        mSimilarMoviesLiveData = Transformations
                .switchMap(mBooleanLiveData) {
                    mMovieRepository.performSimilarMoviesFetch(movieId)
                }
    }

    fun setBooleanLiveData(value: Boolean) {
        mBooleanLiveData.postValue(value)
    }
}