package com.mitash.moviepedia.di

import android.app.Application
import com.mitash.moviepedia.MoviePediaApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            ApiModule::class,
            ActivityBuildersModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(moviePediaApp: MoviePediaApp)
}