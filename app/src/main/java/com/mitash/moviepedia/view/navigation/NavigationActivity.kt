package com.mitash.moviepedia.view.navigation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ActivityNavigationBinding
import com.mitash.moviepedia.util.ActivityUtils
import com.mitash.moviepedia.util.BottomNavigationHelper
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.vo.navigationstack.StackTransaction
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class NavigationActivity : AppCompatActivity(), HasSupportFragmentInjector
        , BottomNavigationView.OnNavigationItemSelectedListener, BackHandledFragment.BackHandlerInterface {

    companion object {
        private const val ACTION_VOICE_SEARCH = "com.google.android.gms.actions.SEARCH_ACTION"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private var mActivityNavigationBinding: ActivityNavigationBinding? = null

    @Inject
    lateinit var mDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var mNavigationViewModel: NavigationViewModel

    private var mSelectedFragment: BackHandledFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityNavigationBinding = DataBindingUtil.setContentView(this, R.layout.activity_navigation)

        mNavigationViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(NavigationViewModel::class.java)

        setupBottomNavigationView()

        if (null == savedInstanceState) {
            ActivityUtils.addFragmentToActivity(supportFragmentManager
                    , NavigationFragment.newInstance()
                    , R.id.container
                    , false
                    , NavigationFragment::class.simpleName)
        }
    }

    private fun setupBottomNavigationView() {
        mActivityNavigationBinding?.bottomNavigationView?.let {
            BottomNavigationHelper.removeShiftMode(it)
        }

        mActivityNavigationBinding?.bottomNavigationView?.setOnNavigationItemSelectedListener(this)

        mNavigationViewModel.getSelectDiscoverMenuEvent().observe(this, Observer {
            mActivityNavigationBinding?.bottomNavigationView?.menu
                    ?.findItem(R.id.action_discover)?.isChecked = true
        })
    }

    override fun onBackPressed() {
        if (null == mSelectedFragment || !mSelectedFragment!!.onBackPressed()) {
            // Selected fragment did not consume the back press event.
            super.onBackPressed()
        }
    }

    override fun supportFragmentInjector() = mDispatchingAndroidInjector

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (mNavigationViewModel.isCurrentTraverseSameAsNavigationClick(item.itemId)) {
            if (null == mSelectedFragment || !mSelectedFragment!!.handleNavigationClick()) {
                // Selected fragment did not consume the navigation click event.
                mNavigationViewModel.performBottomTabFragmentsTransaction(item.itemId)
            }
        } else {
            mNavigationViewModel.performBottomTabFragmentsTransaction(item.itemId)
        }
        return true
    }

    override fun setSelectedFragment(backHandledFragment: BackHandledFragment?) {
        this.mSelectedFragment = backHandledFragment
    }

    override fun popBackStack(): Boolean {
        return mNavigationViewModel.popBackStack()
    }

    override fun triggerStackTransaction(stackTransaction: StackTransaction) {
        mNavigationViewModel.performStackFragmentsTransaction(stackTransaction)
    }
}