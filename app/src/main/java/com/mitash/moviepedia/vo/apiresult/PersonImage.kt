package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/16/2018.
 */
data class PersonImage(
        @SerializedName("id")
        var _id: Int,
        @SerializedName("page")
        var page: Int?,
        @SerializedName("total_results")
        var totalResults: Int?,
        @SerializedName("results")
        var results: List<Results>?,
        @SerializedName("total_pages")
        var totalPages: Int?
) {
    data class Results(
            @SerializedName("vote_count")
            var voteCount: Int?,
            @SerializedName("media_type")
            val mediaType: String?,
            @SerializedName("file_path")
            val filePath: String?,
            @SerializedName("aspect_ratio")
            val aspectRatio: Double?,
            @SerializedName("media")
            var media: Media?,
            @SerializedName("height")
            var height: Int?,
            @SerializedName("vote_average")
            val voteAverage: Double?,
            @SerializedName("width")
            var width: Int?)

    data class Media(
            @SerializedName("popularity")
            val popularity: Double?,
            @SerializedName("vote_count")
            var voteCount: Int?,
            @SerializedName("video")
            var video: Boolean?,
            @SerializedName("poster_path")
            val posterPath: String?,
            @SerializedName("id")
            var _id: Int?,
            @SerializedName("adult")
            var adult: Boolean?,
            @SerializedName("backdrop_path")
            val backdropPath: String?,
            @SerializedName("original_language")
            val originalLanguage: String?,
            @SerializedName("original_title")
            val originalTitle: String?,
            @SerializedName("genre_ids")
            var genreIds: List<Int?>?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("vote_average")
            var voteAverage: Double?,
            @SerializedName("overview")
            val overview: String?,
            @SerializedName("release_date")
            val releaseDate: String?)
}