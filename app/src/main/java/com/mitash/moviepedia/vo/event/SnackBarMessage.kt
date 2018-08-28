package com.mitash.moviepedia.vo.event

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.annotation.StringRes

/**
 * A SingleLiveEvent used for Snackbar messages. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class SnackBarMessage : SingleLiveEvent<Int>() {

    fun observe(owner: LifecycleOwner, observer: SnackBarObserver) {
        super.observe(owner, Observer{ t ->
            if (t == null) {
                return@Observer
            }
            observer.onNewMessage(t)
        })
    }

    interface SnackBarObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param snackBarMessageResourceId The new message, non-null.
         */
        fun onNewMessage(@StringRes snackBarMessageResourceId: Int)
    }

}