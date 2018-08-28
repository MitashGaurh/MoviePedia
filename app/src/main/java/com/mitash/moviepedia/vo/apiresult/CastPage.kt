package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName
import com.mitash.moviepedia.db.entity.Cast
import com.mitash.moviepedia.db.entity.Crew

/**
 * Created by Mitash Gaurh on 8/10/2018.
 */
data class CastPage(
        @field:SerializedName("id")
        val movieId: Int,
        @field:SerializedName("cast")
        val casts: List<Cast>,
        @field:SerializedName("crew")
        val crews: List<Crew>
)