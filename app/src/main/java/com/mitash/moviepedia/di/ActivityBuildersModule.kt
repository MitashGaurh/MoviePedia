package com.mitash.moviepedia.di

import com.mitash.moviepedia.sync.MoviePediaSyncService
import com.mitash.moviepedia.view.navigation.NavigationActivity
import com.mitash.moviepedia.view.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [SplashFragmentBuildersModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNavigationActivity(): NavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeSyncAdapterService(): MoviePediaSyncService
}