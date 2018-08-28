package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mitash.moviepedia.db.entity.Crew

/**
 * Created by Mitash Gaurh on 8/10/2018.
 */
@Dao
abstract class CrewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCrews(crews: List<Crew>)

    @Query("SELECT * FROM crew WHERE movieId =:movieId")
    abstract fun loadCrewsByMovieId(movieId: Int): LiveData<List<Crew>>
}