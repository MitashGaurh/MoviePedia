package com.mitash.moviepedia.view.splash

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Window
import android.view.WindowManager
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.sync.MoviePediaSyncAdapter
import com.mitash.moviepedia.util.ActivityUtils
import com.mitash.moviepedia.util.PreferenceUtil
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.genre.GenreSelectFragment
import com.mitash.moviepedia.view.navigation.NavigationActivity
import com.mitash.moviepedia.vo.AppConstants
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : FragmentActivity(), HasSupportFragmentInjector, BackHandledFragment.BackHandlerInterface {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var mSplashViewModel: SplashViewModel

    @Inject
    lateinit var mAppExecutors: AppExecutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash)

        mSplashViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(SplashViewModel::class.java)

        MoviePediaSyncAdapter.initializeSyncAdapter(this)

        if (null == savedInstanceState) {
            if (PreferenceUtil.contains(this, AppConstants.SharedPrefConstants.SELECTED_GENRES)) {
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            } else {
                mSplashViewModel.initiateFetchCall(this) {
                    if (it) {
                        mAppExecutors.mainThread().execute {
                            Handler().postDelayed({
                                ActivityUtils.addFragmentToActivity(supportFragmentManager
                                        , GenreSelectFragment.newInstance(false)
                                        , R.id.splash_container
                                        , false
                                        , GenreSelectFragment::class.simpleName)
                            }, 2000)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //No call for super(). Bug on API Level > 11.
    }

    override fun supportFragmentInjector() = mDispatchingAndroidInjector

    override fun setSelectedFragment(backHandledFragment: BackHandledFragment?) {

    }

    override fun popBackStack(): Boolean {
        return false
    }

    override fun triggerStackTransaction(fragment: Fragment) {

    }
}
