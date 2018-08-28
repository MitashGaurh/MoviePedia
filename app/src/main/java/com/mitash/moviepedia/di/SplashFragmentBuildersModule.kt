package com.mitash.moviepedia.di

import com.mitash.moviepedia.view.genre.GenreSelectFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Mitash Gaurh on 7/24/2018.
 */
@Suppress("unused")
@Module
abstract class SplashFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeGenreSelectFragment(): GenreSelectFragment
}