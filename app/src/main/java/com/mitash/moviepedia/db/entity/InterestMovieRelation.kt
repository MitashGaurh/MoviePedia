package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
class InterestMovieRelation {

    @Embedded
    var interest: Interest = Interest(0, 0)

    @Relation(parentColumn = "movieId", entityColumn = "_id", entity = Movie::class)
    var movies: List<Movie>? = listOf()
}