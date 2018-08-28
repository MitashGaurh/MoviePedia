package com.mitash.moviepedia.repository

import android.arch.lifecycle.LiveData
import com.mitash.moviepedia.db.MoviePediaDb
import com.mitash.moviepedia.db.entity.Genre
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mitash Gaurh on 8/22/2018.
 */
@Singleton
class MoreRepository @Inject constructor(
        private val mMoviePediaDb: MoviePediaDb
) {
    fun loadSelectedGenres(genreIds: List<Int>): LiveData<List<Genre>>? {
        return mMoviePediaDb.genreDao().loadGenreByIds(genreIds)
    }
}