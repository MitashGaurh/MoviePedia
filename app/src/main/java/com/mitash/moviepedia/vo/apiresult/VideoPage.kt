package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName
import com.mitash.moviepedia.db.entity.Video

/**
 * Created by Mitash Gaurh on 8/8/2018.
 */
data class VideoPage(
        @field:SerializedName("id")
        val movieId: Int,
        @field:SerializedName("results")
        val results: List<Video>
)
