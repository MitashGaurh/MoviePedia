package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.mitash.moviepedia.db.entity.Interest
import com.mitash.moviepedia.db.entity.InterestGenreRelation


/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
@Dao
abstract class InterestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertInterests(interests: List<Interest>)

    @Transaction
    @Query("SELECT * FROM genre WHERE _id IN (:genreIds)")
    abstract fun loadMoviesBasedOnGenreIds(genreIds: List<Int>): LiveData<List<InterestGenreRelation>>

    @Query("DELETE FROM interest")
    abstract fun nukeTable()
}