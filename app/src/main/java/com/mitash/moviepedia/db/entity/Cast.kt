package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/10/2018.
 */
@Entity(tableName = "cast", primaryKeys = ["castId", "movieId"])
data class Cast(
        @field:SerializedName("cast_id")
        val castId: Int,
        @field:SerializedName("character")
        val character: String?,
        @field:SerializedName("credit_id")
        val creditId: String?,
        @field:SerializedName("gender")
        val gender: Int?,
        @field:SerializedName("id")
        val id: Int?,
        @field:SerializedName("name")
        val name: String?,
        @field:SerializedName("order")
        val order: Int?,
        @field:SerializedName("profile_path")
        val profilePath: String?,
        var movieId: Int
)