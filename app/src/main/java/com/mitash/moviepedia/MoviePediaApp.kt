package com.mitash.moviepedia

import android.app.Activity
import android.app.Application
import android.app.Service
import com.facebook.stetho.Stetho
import com.mitash.moviepedia.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter
import javax.inject.Inject

/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
class MoviePediaApp : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var mDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mServiceDispatchingInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, exception ->

            val sw = StringWriter()
            exception.printStackTrace(PrintWriter(sw))
            val exceptionAsString = sw.toString()
            Timber.e("  ---->  %s", exceptionAsString)
            Timber.e("uncaughtException: Exception ENDS")
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
        AppInjector.init(this)
    }

    override fun activityInjector() = mDispatchingAndroidInjector

    override fun serviceInjector() = mServiceDispatchingInjector

}