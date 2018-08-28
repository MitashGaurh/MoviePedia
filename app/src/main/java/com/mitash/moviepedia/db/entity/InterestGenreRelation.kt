package com.mitash.moviepedia.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Created by Mitash Gaurh on 8/20/2018.
 */
class InterestGenreRelation {

    @Embedded
    var genre: Genre = Genre(0, "")

    @Relation(parentColumn = "_id", entityColumn = "genreId", entity = Interest::class)
    var interests: List<Interest>? = listOf()

    @Relation(parentColumn = "_id", entityColumn = "genreId", entity = Interest::class)
    var interestMovies: List<InterestMovieRelation>? = listOf()
}