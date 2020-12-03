package com.mitash.moviepedia.sync

import android.accounts.Account
import android.accounts.AccountManager
import android.content.*
import android.os.Bundle
import com.mitash.moviepedia.R
import com.mitash.moviepedia.repository.DiscoverRepository
import com.mitash.moviepedia.util.AppUtil
import javax.inject.Inject

/**
 * Set up the sync adapter
 */
class MoviePediaSyncAdapter @Inject constructor(
        mContext: Context, mAutoInitialize: Boolean, private val mDiscoverRepository: DiscoverRepository
) : AbstractThreadedSyncAdapter(mContext, mAutoInitialize) {

    override fun onPerformSync(account: Account?, extras: Bundle?, authority: String?, provider: ContentProviderClient?, syncResult: SyncResult?) {

        if (AppUtil.shouldTriggerSync(context, true)) {
            mDiscoverRepository.executeGenreFetch(context, true) {}
        }
    }

    companion object {
        private const val TAG = "MoviePediaSyncAdapter"

        private const val SYNC_INTERVAL: Long = 60 * 60// 60 seconds
        private const val SYNC_FLEXTIME: Long = SYNC_INTERVAL / 3

        fun initializeSyncAdapter(context: Context) {
            getSyncAccount(context)
        }

        private fun getSyncAccount(context: Context): Account? {
            val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
            val newAccount = Account(context.getString(R.string.app_name), context.getString(R.string.account_type))
            try {
                if (null == accountManager.getPassword(newAccount)) {
                    if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                        return null
                    }
                    onAccountCreated(newAccount, context)
                }
            } catch (e: SecurityException) {
                if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                    return null
                }
                onAccountCreated(newAccount, context)
            }
            return newAccount
        }

        private fun onAccountCreated(newAccount: Account, context: Context) {
            MoviePediaSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME)
            ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.authority), true)
            //syncImmediately(context)
        }

        private fun configurePeriodicSync(context: Context, syncInterval: Long, syncFlextime: Long) {
            val account = getSyncAccount(context)
            val authority = context.getString(R.string.authority)

            val request = SyncRequest.Builder()
                    .syncPeriodic(syncInterval, syncFlextime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle())
                    .build()
            ContentResolver.requestSync(request)

        }

        fun syncImmediately(context: Context) {
            val bundle = Bundle()
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.authority), bundle)
        }
    }

}