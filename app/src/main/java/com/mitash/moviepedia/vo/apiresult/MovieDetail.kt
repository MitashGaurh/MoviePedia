package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName
import com.mitash.moviepedia.db.entity.Genre

/**
 * Created by Mitash Gaurh on 8/13/2018.
 */
data class MovieDetail(
        @field:SerializedName("adult")
        val adult: Boolean?,
        @field:SerializedName("backdrop_path")
        val backdropPath: String?,
        @field:SerializedName("belongs_to_collection")
        val belongsToCollection: BelongsToCollection?,
        @field:SerializedName("budget")
        val budget: Int?,
        @field:SerializedName("genres")
        val genres: List<Genre>?,
        @field:SerializedName("homepage")
        val homepage: String?,
        @field:SerializedName("id")
        val _id: Int?,
        @field:SerializedName("imdb_id")
        val imdbId: String?,
        @field:SerializedName("original_language")
        val originalLanguage: String?,
        @field:SerializedName("original_title")
        val originalTitle: String?,
        @field:SerializedName("overview")
        val overview: String?,
        @field:SerializedName("popularity")
        val popularity: Double?,
        @field:SerializedName("poster_path")
        val posterPath: String?,
        @field:SerializedName("production_companies")
        val productionCompanies: List<ProductionCompany>?,
        @field:SerializedName("production_countries")
        val productionCountries: List<ProductionCountry>?,
        @field:SerializedName("release_date")
        val releaseDate: String?,
        @field:SerializedName("revenue")
        val revenue: Long?,
        @field:SerializedName("runtime")
        val runtime: Int?,
        @field:SerializedName("spoken_languages")
        val spokenLanguages: List<SpokenLanguage>?,
        @field:SerializedName("status")
        val status: String?,
        @field:SerializedName("tagline")
        val tagLine: String?,
        @field:SerializedName("title")
        val title: String?,
        @field:SerializedName("video")
        val video: Boolean?,
        @field:SerializedName("vote_average")
        val voteAverage: Double?,
        @field:SerializedName("vote_count")
        val voteCount: Int?
) {
    data class BelongsToCollection(
            @field:SerializedName("id")
            val _id: Int?,
            @field:SerializedName("name")
            val name: String?,
            @field:SerializedName("poster_path")
            val posterPath: String?,
            @field:SerializedName("backdrop_path")
            val backdropPath: String?
    )

    data class ProductionCompany(
            @field:SerializedName("id")
            val _id: Int?,
            @field:SerializedName("logo_path")
            val logoPath: String?,
            @field:SerializedName("name")
            val name: String?,
            @field:SerializedName("origin_country")
            val originCountry: String?
    )

    data class ProductionCountry(
            @field:SerializedName("iso_3166_1")
            val iso_3166_1: String?,
            @field:SerializedName("name")
            val name: String?
    )

    data class SpokenLanguage(
            @field:SerializedName("iso_639_1")
            val iso_639_1: String?,
            @field:SerializedName("name")
            val name: String?
    )
}