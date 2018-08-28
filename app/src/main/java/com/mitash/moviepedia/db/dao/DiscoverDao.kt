package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mitash.moviepedia.db.entity.Discover
import com.mitash.moviepedia.db.entity.DiscoverMovieRelation

/**
 * Created by Mitash Gaurh on 7/23/2018.
 */
@Dao
abstract class DiscoverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertHeaders(discovers: List<Discover>)

    @Query("SELECT * FROM discover")
    abstract fun loadHeaders(): LiveData<List<Discover>>

    @Transaction
    @Query("SELECT * FROM discover")
    abstract fun loadDiscoverWithMovies(): LiveData<List<DiscoverMovieRelation>>

    @Query("DELETE FROM discover WHERE fetchType =:fetchType")
    abstract fun deleteHeaderByFetchType(fetchType: String)
}