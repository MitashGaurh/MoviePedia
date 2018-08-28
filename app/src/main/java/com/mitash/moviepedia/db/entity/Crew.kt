package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/10/2018.
 */
@Entity(tableName = "crew", primaryKeys = ["creditId", "movieId"])
data class Crew(
        @field:SerializedName("credit_id")
        val creditId: String,
        @field:SerializedName("department")
        val department: String?,
        @field:SerializedName("gender")
        val gender: Int?,
        @field:SerializedName("id")
        val id: Int?,
        @field:SerializedName("job")
        val job: String?,
        @field:SerializedName("name")
        val name: String?,
        @field:SerializedName("profile_path")
        val profilePath: String?,
        var movieId: Int
)