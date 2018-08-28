package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Created by Mitash Gaurh on 7/23/2018.
 */
class DiscoverMovieRelation {
    @Embedded
    var discover: Discover = Discover("", "")

    @Relation(parentColumn = "fetchType", entityColumn = "fetchType", entity = Movie::class)
    var optionNameList: List<Movie>? = listOf()
}