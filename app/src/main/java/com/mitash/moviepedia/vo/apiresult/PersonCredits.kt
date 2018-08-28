package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/17/2018.
 */
data class PersonCredits(
        @SerializedName("cast")
        var cast: List<Cast>?
) {
    data class Cast(
            @SerializedName("id")
            var _id: Int,
            @SerializedName("character")
            val character: String?,
            @SerializedName("original_title")
            val originalTitle: String?,
            @SerializedName("overview")
            val overview: String?,
            @SerializedName("vote_count")
            var voteCount: Int?,
            @SerializedName("video")
            var video: Boolean?,
            @SerializedName("media_type")
            val mediaType: String?,
            @SerializedName("release_date")
            val releaseDate: String?,
            @SerializedName("vote_average")
            val voteAverage: Double?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("popularity")
            val popularity: Double?,
            @SerializedName("original_language")
            val originalLanguage: String?,
            @SerializedName("genre_ids")
            var genreIds: List<Int>?,
            @SerializedName("backdrop_path")
            val backdropPath: String?,
            @SerializedName("adult")
            var adult: Boolean?,
            @SerializedName("poster_path")
            val posterPath: String?,
            @SerializedName("credit_id")
            val creditId: String?)
}