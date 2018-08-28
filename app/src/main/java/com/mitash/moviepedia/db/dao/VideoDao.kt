package com.mitash.moviepedia.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mitash.moviepedia.db.entity.Video

/**
 * Created by Mitash Gaurh on 8/8/2018.
 */
@Dao
abstract class VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertVideos(videos: List<Video>)

    @Query("SELECT * FROM video WHERE movieId =:movieId")
    abstract fun loadVideosByMovieId(movieId: Int): LiveData<List<Video>>
}