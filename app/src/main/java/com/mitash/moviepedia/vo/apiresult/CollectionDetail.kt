package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/17/2018.
 */
data class CollectionDetail(
        @SerializedName("id")
        var id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("overview")
        val overview: String?,
        @SerializedName("poster_path")
        val posterPath: String?,
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        @SerializedName("parts")
        var parts: List<Parts>?
) {
    data class Parts(
            @SerializedName("adult")
            var adult: Boolean?,
            @SerializedName("backdrop_path")
            val backdrop_path: String?,
            @SerializedName("genre_ids")
            var genreIds: List<Int>?,
            @SerializedName("id")
            var _id: Int,
            @SerializedName("original_language")
            val originalLanguage: String?,
            @SerializedName("original_title")
            val originalTitle: String?,
            @SerializedName("overview")
            val overview: String?,
            @SerializedName("poster_path")
            val posterPath: String?,
            @SerializedName("release_date")
            val releaseDate: String?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("video")
            var video: Boolean?,
            @SerializedName("vote_average")
            val voteAverage: Double?,
            @SerializedName("vote_count")
            var voteCount: Int?,
            @SerializedName("popularity")
            val popularity: Double?
    )
}