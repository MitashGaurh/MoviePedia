package com.mitash.moviepedia.view.discover

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentDiscoverBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.PreferenceUtil
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.movie.MovieFragment
import com.mitash.moviepedia.view.navigation.NavigationActivity
import com.mitash.moviepedia.vo.AppConstants
import com.mitash.moviepedia.vo.navigationstack.Traverse
import com.mitash.moviepedia.vo.stringSetLiveData
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class DiscoverFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "DiscoverFragment"

        fun newInstance(): DiscoverFragment = DiscoverFragment()
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mDiscoverViewModel: DiscoverViewModel

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private var mBinding by autoCleared<FragmentDiscoverBinding>()

    private var mAdapter by autoCleared<DiscoverVerticalAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentDiscoverBinding>(
                inflater,
                R.layout.fragment_discover,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDiscoverViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(DiscoverViewModel::class.java)


        if (!PreferenceUtil.contains(activity!!, AppConstants.SharedPrefConstants.MOVIE_FETCH_COMPLETED)
                || !PreferenceUtil[activity!!, AppConstants.SharedPrefConstants.MOVIE_FETCH_COMPLETED, false]) {
            mDiscoverViewModel.initiateFetchMoviesCall(activity!!) {
                if (it) {
                    PreferenceUtil[activity!!, AppConstants.SharedPrefConstants.MOVIE_FETCH_COMPLETED] = true
                    mDiscoverViewModel.setBooleanLiveData(true)
                }
            }
        } else {
            mDiscoverViewModel.setBooleanLiveData(true)
        }

        (activity as NavigationActivity).setSupportActionBar(mBinding.toolbar)
        (activity as NavigationActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        initRecyclerView()

        subscribeToLiveData()

        subscribeToSharedPreferenceLiveData()
    }

    private fun initRecyclerView() {
        val adapter = DiscoverVerticalAdapter(mAppExecutors) { movie ->
            onQueueTransaction(MovieFragment.newInstance(movie._id, movie.title!!, movie.fetchType, Traverse.DISCOVER.value))
        }
        this.mAdapter = adapter

        mBinding.rvVerticalDiscover.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        mBinding.rvVerticalDiscover.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)

        mBinding.rvVerticalDiscover.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mDiscoverViewModel.mDiscoverWithMoviesLiveData.observe(this, Observer {
            if (null != it && it.isNotEmpty()) {
                mAdapter.submitList(it)
            } else {
                mAdapter.submitList(emptyList())
            }
        })
    }

    private fun subscribeToSharedPreferenceLiveData() {
        PreferenceUtil.getSharePreferenceInstance(context!!)
                .stringSetLiveData(AppConstants.SharedPrefConstants.SELECTED_GENRES, HashSet())
                .observe(this, Observer {
                    if (null != it && it.isNotEmpty()) {
                        mDiscoverViewModel.initiateGenreInterestCall(context!!) { success ->
                            if (success) {
                                mDiscoverViewModel.setBooleanLiveData(true)
                            }
                        }
                    } else {
                        mDiscoverViewModel.nukeGenreInterestData()
                    }
                })
    }

    override fun onBackPressed(): Boolean {
        val layoutManager = mBinding.rvVerticalDiscover.layoutManager as LinearLayoutManager
        return if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            false
        } else {
            mBinding.rvVerticalDiscover.smoothScrollToPosition(0)
            true
        }
    }

    override fun handleNavigationClick(): Boolean {
        val layoutManager = mBinding.rvVerticalDiscover.layoutManager as LinearLayoutManager
        return if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            false
        } else {
            mBinding.rvVerticalDiscover.smoothScrollToPosition(0)
            true
        }
    }
}
