package com.mitash.moviepedia.vo.apiresult

import com.google.gson.annotations.SerializedName

/**
 * Created by Mitash Gaurh on 8/10/2018.
 */
data class RequestToken(
        @field:SerializedName("success")
        val success: Boolean,
        @field:SerializedName("expires_at")
        val expiresAt: String?,
        @field:SerializedName("request_token")
        val requestToken: String?
)