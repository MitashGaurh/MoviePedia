package com.mitash.moviepedia.sync

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.content.Context
import android.os.Bundle

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
class MoviePediaAuthenticator(context: Context) : AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(p0: String?): String? {
        return null
    }

    override fun confirmCredentials(p0: AccountAuthenticatorResponse?, p1: Account?, p2: Bundle?): Bundle? {
        return null
    }

    override fun updateCredentials(p0: AccountAuthenticatorResponse?, p1: Account?, p2: String?, p3: Bundle?): Bundle? {
        return null
    }

    override fun getAuthToken(p0: AccountAuthenticatorResponse?, p1: Account?, p2: String?, p3: Bundle?): Bundle? {
        return null
    }

    override fun hasFeatures(p0: AccountAuthenticatorResponse?, p1: Account?, p2: Array<out String>?): Bundle? {
        return null
    }

    override fun editProperties(p0: AccountAuthenticatorResponse?, p1: String?): Bundle? {
        return null
    }

    override fun addAccount(p0: AccountAuthenticatorResponse?, p1: String?, p2: String?, p3: Array<out String>?, p4: Bundle?): Bundle? {
        return null
    }
}