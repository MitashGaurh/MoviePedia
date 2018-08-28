package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
@Entity(tableName = "movie", primaryKeys = ["_id", "fetchType"])
data class Movie(
        @field:SerializedName("id")
        val _id: Int,
        @field:SerializedName("title")
        val title: String?,
        @field:SerializedName("poster_path")
        val posterPath: String?,
        @field:SerializedName("video")
        val video: Boolean,
        @field:SerializedName("vote_count")
        val voteCount: Int?,
        @field:SerializedName("vote_average")
        val voteAverage: Double?,
        @field:SerializedName("popularity")
        val popularity: Double?,
        @field:SerializedName("backdrop_path")
        val backdropPath: String?,
        @field:SerializedName("overview")
        val overview: String?,
        @field:SerializedName("adult")
        val adult: Boolean?,
        @field:SerializedName("release_date")
        val releaseDate: String?,
        var fetchType: String
)
