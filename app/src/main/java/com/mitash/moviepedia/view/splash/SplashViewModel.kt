package com.mitash.moviepedia.view.splash

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.mitash.moviepedia.repository.DiscoverRepository
import com.mitash.moviepedia.util.AppUtil
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 7/24/2018.
 */
class SplashViewModel @Inject constructor(
        private val mDiscoverRepository: DiscoverRepository
) : ViewModel() {

    fun initiateFetchCall(context: Context, callback: (Boolean) -> Unit) {
        if (AppUtil.shouldTriggerSync(context, true)) {
            mDiscoverRepository.executeGenreFetch(context, false) {
                callback(it)
            }
        }
    }

    fun startLoginProcess() {
        mDiscoverRepository.obtainNewRequestToken {
            if (it.success) {

            }
        }
    }
}