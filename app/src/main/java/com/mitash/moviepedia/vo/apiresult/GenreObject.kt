package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName
import com.mitash.moviepedia.db.entity.Genre

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
data class GenreObject(
        @field:SerializedName("genres")
        val genres: List<Genre>
)