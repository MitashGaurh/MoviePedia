package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mitash.moviepedia.db.entity.Cast

/**
 * Created by Mitash Gaurh on 8/10/2018.
 */
@Dao
abstract class CastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCasts(casts: List<Cast>)

    @Query("SELECT * FROM `cast` WHERE movieId =:movieId LIMIT 20")
    abstract fun loadCastsByMovieId(movieId: Int): LiveData<List<Cast>>
}