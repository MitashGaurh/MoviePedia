package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/16/2018.
 */
data class PersonDetail(
        @SerializedName("birthday")
        val birthday: String?,
        @SerializedName("known_for_department")
        val knownForDepartment: String?,
        @SerializedName("deathday")
        val deathDay: String?,
        @SerializedName("id")
        val _id: Int,
        @SerializedName("name")
        val name: String?,
        @SerializedName("also_known_as")
        val alsoKnownAs: List<String>?,
        @SerializedName("gender")
        val gender: Int?,
        @SerializedName("biography")
        val biography: String?,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("place_of_birth")
        val placeOfBirth: String?,
        @SerializedName("profile_path")
        val profilePath: String?,
        @SerializedName("adult")
        val adult: Boolean,
        @SerializedName("imdb_id")
        val imdbId: String?,
        @SerializedName("homepage")
        val homepage: String?
)