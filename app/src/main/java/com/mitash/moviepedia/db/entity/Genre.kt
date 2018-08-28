package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
@Entity(tableName = "genre", primaryKeys = ["_id"])
data class Genre(
        @field:SerializedName("id")
        val _id: Int,
        @field:SerializedName("name")
        val name: String?
) {
    @Ignore
    var isSelected: Boolean = false
}