package com.mitash.moviepedia.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.mitash.moviepedia.view.collection.CollectionViewModel
import com.mitash.moviepedia.view.discover.DiscoverViewModel
import com.mitash.moviepedia.view.genre.GenreViewModel
import com.mitash.moviepedia.view.interest.InterestViewModel
import com.mitash.moviepedia.view.more.MoreViewModel
import com.mitash.moviepedia.view.movie.MovieViewModel
import com.mitash.moviepedia.view.navigation.NavigationViewModel
import com.mitash.moviepedia.view.person.PersonViewModel
import com.mitash.moviepedia.view.search.SearchViewModel
import com.mitash.moviepedia.view.splash.SplashViewModel
import com.mitash.moviepedia.viewmodel.MoviePediaViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationViewModel::class)
    abstract fun bindNavigationViewModel(navigationViewModel: NavigationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverViewModel::class)
    abstract fun bindDiscoverModel(discoverViewModel: DiscoverViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GenreViewModel::class)
    abstract fun bindGenreViewModel(genreViewModel: GenreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    abstract fun bindMovieViewModel(movieViewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonViewModel::class)
    abstract fun bindPersonViewModel(personViewModel: PersonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel::class)
    abstract fun bindCollectionViewModel(collectionViewModel: CollectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InterestViewModel::class)
    abstract fun bindInterestViewModel(interestViewModel: InterestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindMoreViewModel(moreViewModel: MoreViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: MoviePediaViewModelFactory): ViewModelProvider.Factory
}