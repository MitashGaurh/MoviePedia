package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName
import com.mitash.moviepedia.db.entity.Movie

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
data class Page(
        @field:SerializedName("page")
        val page: Int,
        @field:SerializedName("total_results")
        val totalResults: Int?,
        @field:SerializedName("total_pages")
        val totalPages: Int?,
        @field:SerializedName("results")
        val results: List<Movie>
)
