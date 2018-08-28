package com.mitash.moviepedia.di

import android.app.Application
import android.arch.persistence.room.Room
import com.mitash.moviepedia.db.MoviePediaDb
import com.mitash.moviepedia.db.dao.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): MoviePediaDb {
        return Room.databaseBuilder(app, MoviePediaDb::class.java, "movie_pedia.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(db: MoviePediaDb): MovieDao {
        return db.movieDao()
    }

    @Singleton
    @Provides
    fun provideGenreDao(db: MoviePediaDb): GenreDao {
        return db.genreDao()
    }

    @Singleton
    @Provides
    fun provideDiscoverDao(db: MoviePediaDb): DiscoverDao {
        return db.discoverDao()
    }

    @Singleton
    @Provides
    fun provideSearchDao(db: MoviePediaDb): SearchDao {
        return db.searchDao()
    }

    @Singleton
    @Provides
    fun provideVideoDao(db: MoviePediaDb): VideoDao {
        return db.videoDao()
    }

    @Singleton
    @Provides
    fun provideCastDao(db: MoviePediaDb): CastDao {
        return db.castDao()
    }

    @Singleton
    @Provides
    fun provideCrewDao(db: MoviePediaDb): CrewDao {
        return db.crewDao()
    }

    @Singleton
    @Provides
    fun provideInterestDao(db: MoviePediaDb): InterestDao {
        return db.interestDao()
    }

    @Singleton
    @Provides
    fun provideBoolean(): Boolean = true

    @Singleton
    @Provides
    fun provideContext(app: Application) = app.applicationContext!!
}