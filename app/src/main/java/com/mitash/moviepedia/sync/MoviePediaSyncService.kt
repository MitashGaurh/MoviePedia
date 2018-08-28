package com.mitash.moviepedia.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
class MoviePediaSyncService : Service() {

    @Inject
    lateinit var mMoviePediaSyncAdapter: MoviePediaSyncAdapter

    override fun onCreate() {
        AndroidInjection.inject(this)
    }

    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    override fun onBind(intent: Intent): IBinder {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the MoviePediaSyncAdapter
         * constructors call super()
         */
        return mMoviePediaSyncAdapter.syncAdapterBinder
    }
}