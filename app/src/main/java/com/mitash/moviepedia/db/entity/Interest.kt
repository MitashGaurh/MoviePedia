package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
@Entity(tableName = "interest")
data class Interest(
        val movieId: Int?,
        val genreId: Int?,
        @PrimaryKey(autoGenerate = true)
        val _id: Int = 0
)