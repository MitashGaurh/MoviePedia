package com.mitash.moviepedia.vo

/**
 * Created by Mitash Gaurh on 7/23/2018.
 */
class AppConstants {

    companion object {
        const val YOUTUBE_API_KEY = "AIzaSyBBRZFJr_4S6iQGMrer9JF2rZ8RVrdzGyc"
    }

    class SetupConstants {
        companion object {
            const val FETCH_TYPE_TRENDING = "FETCH_TYPE.TRENDING"
            const val FETCH_TYPE_MOST_POPULAR = "FETCH_TYPE.MOST_POPULAR"
            const val FETCH_TYPE_BEST_FROM_YEAR = "FETCH_TYPE.BEST_FROM_YEAR"
            const val FETCH_TYPE_NOW_PLAYING = "FETCH_TYPE.NOW_PLAYING"
            const val FETCH_TYPE_TOP_RATED = "FETCH_TYPE.TOP_RATED"
            const val FETCH_TYPE_UPCOMING = "FETCH_TYPE.UPCOMING"
            const val FETCH_TYPE_GENRE_INTEREST_MOVIES = "FETCH_TYPE.GENRE_INTEREST_MOVIES"
            const val FETCH_TYPE_INTEREST_MOVIES = "FETCH_TYPE.INTEREST_MOVIES"
        }
    }

    class SharedPrefConstants {
        companion object {
            const val LAST_SETUP_FETCH = "LAST_SETUP_FETCH"
            const val MOVIE_FETCH_COMPLETED = "MOVIE_FETCH_COMPLETED"
            const val INTEREST_FETCH_COMPLETED = "INTEREST_FETCH_COMPLETED"
            const val SELECTED_GENRES = "SELECTED_GENRES"
        }
    }

    class SearchConstants {
        companion object {
            const val MEDIA_TYPE_MOVIE = "movie"
            const val MEDIA_TYPE_TV = "tv"
            const val MEDIA_TYPE_PERSON = "person"
        }
    }
}