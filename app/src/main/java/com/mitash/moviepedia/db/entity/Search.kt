package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 7/26/2018.
 */
@Entity(tableName = "search", primaryKeys = ["_id"])
data class Search(
        @field:SerializedName("id")
        val _id: Int,
        @field:SerializedName("name")
        val name: String?,
        @field:SerializedName("title")
        val title: String?,
        @field:SerializedName("media_type")
        val mediaType: String?,
        @field:SerializedName("profile_path")
        val profilePath: String?,
        @field:SerializedName("poster_path")
        val posterPath: String?
)