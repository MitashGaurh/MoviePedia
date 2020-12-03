package com.mitash.moviepedia.api

import android.arch.lifecycle.LiveData
import com.mitash.moviepedia.vo.apiresult.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
interface MoviePediaService {

    enum class SortBy(var value: String) {
        RELEASE_DATE_ASCENDING("release_date.asc"),
        RELEASE_DATE_DESCENDING("release_date.desc"),
        POPULARITY_ASCENDING("popularity.asc"),
        POPULARITY_DESCENDING("popularity.desc"),
        VOTE_AVERAGE_ASCENDING("vote_average.asc"),
        VOTE_AVERAGE_DESCENDING("vote_average.desc"),
        VOTE_COUNT_ASCENDING("vote_count.asc"),
        VOTE_COUNT_DESCENDING("vote_count.desc");

        override fun toString(): String {
            return this.value
        }
    }

    @GET("/3/authentication/token/new")
    fun generateNewRequestToken(): Call<RequestToken>


    @GET("/3/genre/movie/list")
    fun fetchGenres(): Call<GenreObject>

    @GET("/3/movie/popular")
    fun fetchPopularMovies(): Call<Page>

    @GET("/3/movie/top_rated")
    fun fetchTopRatedMovies(): Call<Page>

    @GET("/3/discover/movie")
    fun fetchBestMoviesFromYear(@Query("primary_release_year") primaryReleaseYear: Int
                                , @Query("sort_by") sortBy: SortBy
                                , @Query("vote_count.gte") voteCountStartBound: Int): Call<Page>


    @GET("/3/discover/movie/")
    fun fetchTrendingMovies(@Query("sort_by") sortBy: SortBy
                            , @Query("primary_release_date.gte") releaseDateStart: String
                            , @Query("primary_release_date.lte") releaseDateEnd: String): Call<Page>

    @GET("/3/movie/now_playing")
    fun fetchNowPlayingMovies(): Call<Page>

    @GET("/3/movie/upcoming")
    fun fetchUpcomingMovies(): Call<Page>

    @GET("/3/discover/movie")
    fun fetchGenreInterestMovies(@Query("with_genres") withGenres: String
                                 , @Query("sort_by") sortBy: SortBy
                                 , @Query("vote_count.gte") voteCountStartBound: Int): Call<Page>

    @GET("/3/search/multi")
    fun multiSearch(@Query("query") query: String): LiveData<ApiResponse<SearchPage>>


    @GET("/3/movie/{movie_id}")
    fun fetchMovieDetails(@Path("movie_id") movie_id: Int): Call<MovieDetail>

    @GET("/3/movie/{movie_id}/videos")
    fun fetchMovieVideos(@Path("movie_id") movie_id: Int): LiveData<ApiResponse<VideoPage>>

    @GET("/3/movie/{movie_id}/credits")
    fun fetchMovieCast(@Path("movie_id") movie_id: Int): LiveData<ApiResponse<CastPage>>

    @GET("/3/movie/{movie_id}/similar")
    fun fetchSimilarMovie(@Path("movie_id") movie_id: Int): Call<Page>

    @GET("/3/person/{person_id}")
    fun fetchPersonDetails(@Path("person_id") person_id: Int): Call<PersonDetail>

    @GET("/3/person/{person_id}/tagged_images")
    fun fetchPersonImages(@Path("person_id") person_id: Int): Call<PersonImage>

    @GET("/3/person/{person_id}/combined_credits")
    fun fetchPersonCredits(@Path("person_id") person_id: Int): Call<PersonCredits>

    @GET("/3/collection/{collection_id}")
    fun fetchCollectionDetails(@Path("collection_id") collection_id: Int): Call<CollectionDetail>

    @GET("/collection/{collection_id}/images")
    fun fetchCollectionImages(@Path("collection_id") collection_id: Int): Call<CollectionDetail>
}