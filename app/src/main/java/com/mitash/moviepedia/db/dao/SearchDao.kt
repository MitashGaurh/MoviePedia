package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mitash.moviepedia.db.entity.Search

/**
 * Created by Mitash Gaurh on 7/26/2018.
 */
@Dao
abstract class SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSearchResults(searchResults: List<Search>)

    @Query("SELECT * FROM search")
    abstract fun loadSearchResults(): LiveData<List<Search>>

    @Query("DELETE FROM search")
    abstract fun nukeTable()
}