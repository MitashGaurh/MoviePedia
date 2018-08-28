package com.mitash.moviepedia.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mitash.moviepedia.db.dao.*
import com.mitash.moviepedia.db.entity.*

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Database(
        entities = [
            Movie::class,
            Genre::class,
            Discover::class,
            Search::class,
            Video::class,
            Cast::class,
            Crew::class,
            Interest::class],
        version = 1,
        exportSchema = false
)
abstract class MoviePediaDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun genreDao(): GenreDao

    abstract fun discoverDao(): DiscoverDao

    abstract fun searchDao(): SearchDao

    abstract fun videoDao(): VideoDao

    abstract fun castDao(): CastDao

    abstract fun crewDao(): CrewDao

    abstract fun interestDao(): InterestDao
}