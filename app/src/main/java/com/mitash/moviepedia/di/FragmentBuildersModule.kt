package com.mitash.moviepedia.di

import com.mitash.moviepedia.view.collection.CollectionFragment
import com.mitash.moviepedia.view.discover.DiscoverFragment
import com.mitash.moviepedia.view.genre.GenreSelectFragment
import com.mitash.moviepedia.view.interest.InterestEditFragment
import com.mitash.moviepedia.view.interest.InterestFragment
import com.mitash.moviepedia.view.more.MoreFragment
import com.mitash.moviepedia.view.movie.MovieFragment
import com.mitash.moviepedia.view.navigation.NavigationFragment
import com.mitash.moviepedia.view.person.PersonFragment
import com.mitash.moviepedia.view.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeNavigationFragment(): NavigationFragment

    @ContributesAndroidInjector
    abstract fun contributeDiscoverFragment(): DiscoverFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieFragment(): MovieFragment

    @ContributesAndroidInjector
    abstract fun contributeGenreSelectFragment(): GenreSelectFragment

    @ContributesAndroidInjector
    abstract fun contributePersonFragment(): PersonFragment

    @ContributesAndroidInjector
    abstract fun contributeCollectionFragment(): CollectionFragment

    @ContributesAndroidInjector
    abstract fun contributeInterestFragment(): InterestFragment

    @ContributesAndroidInjector
    abstract fun contributeMoreFragment(): MoreFragment

    @ContributesAndroidInjector
    abstract fun contributeInterestEditFragment(): InterestEditFragment
}