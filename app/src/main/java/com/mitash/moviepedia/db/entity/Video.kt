package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/8/2018.
 */
@Entity(tableName = "video", primaryKeys = ["_id"])
data class Video(
        @field:SerializedName("id")
        val _id: String,
        @field:SerializedName("key")
        val key: String?,
        @field:SerializedName("name")
        val name: String?,
        @field:SerializedName("site")
        val site: String?,
        @field:SerializedName("size")
        val size: Int?,
        @field:SerializedName("type")
        val type: String?,
        var movieId: Int
)