package com.mitash.moviepedia.sync

import android.app.Service
import android.content.Intent

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
class MoviePediaAuthenticatorService : Service() {

    lateinit var mAuthenticator: MoviePediaAuthenticator

    override fun onCreate() {
        mAuthenticator = MoviePediaAuthenticator(this)
    }

    override fun onBind(p0: Intent?) = mAuthenticator.iBinder!!
}