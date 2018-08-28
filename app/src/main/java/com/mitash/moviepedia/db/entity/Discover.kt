package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Mitash Gaurh on 7/23/2018.
 */
@Entity(tableName = "discover")
data class Discover(
        @PrimaryKey()
        val fetchType: String,
        val header: String
)