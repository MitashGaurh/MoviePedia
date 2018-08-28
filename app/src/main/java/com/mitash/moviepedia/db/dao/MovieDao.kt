package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mitash.moviepedia.db.entity.Movie

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Dao
abstract class MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movie")
    abstract fun loadMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie WHERE _id =:id AND fetchType =:fetchType")
    abstract fun loadMovieByCriteria(id: Int, fetchType: String): LiveData<Movie>

    @Query("DELETE FROM movie WHERE fetchType =:fetchType")
    abstract fun deleteMovieByFetchType(fetchType: String)
}