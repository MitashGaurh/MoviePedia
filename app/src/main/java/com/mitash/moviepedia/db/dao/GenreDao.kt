package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mitash.moviepedia.db.entity.Genre

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
@Dao
abstract class GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertGenres(genres: List<Genre>)

    @Query("SELECT * FROM genre")
    abstract fun loadGenreList(): LiveData<List<Genre>>

    @Query("SELECT * FROM genre WHERE name IN (:genreNames)")
    abstract fun loadGenreByName(genreNames: Array<String>): List<Genre>

    @Query("SELECT * FROM genre WHERE _id IN (:genreIds)")
    abstract fun loadGenreByIds(genreIds: List<Int>): LiveData<List<Genre>>
}