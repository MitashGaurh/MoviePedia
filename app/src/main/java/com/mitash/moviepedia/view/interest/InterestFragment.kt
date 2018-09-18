package com.mitash.moviepedia.view.interest


import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentInterestBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.AppUtil
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
class InterestFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "InterestFragment"

        fun newInstance(): InterestFragment = InterestFragment()
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private var mInterestViewModel: InterestViewModel? = null

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private var mBinding by autoCleared<FragmentInterestBinding>()

    private var mAdapter by autoCleared<InterestVerticalAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentInterestBinding>(
                inflater,
                R.layout.fragment_interest,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mInterestViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(InterestViewModel::class.java)

        if (!PreferenceUtil.contains(activity!!, AppConstants.SharedPrefConstants.INTEREST_FETCH_COMPLETED)
                || !PreferenceUtil[activity!!, AppConstants.SharedPrefConstants.INTEREST_FETCH_COMPLETED, false]) {
            val selectedGenreString = AppUtil.provideSelectedGenresString(context!!)
            if (null != selectedGenreString) {
                mInterestViewModel?.initiateFetchInterestCall(selectedGenreString) {
                    if (it) {
                        PreferenceUtil[activity!!, AppConstants.SharedPrefConstants.INTEREST_FETCH_COMPLETED] = true
                        mInterestViewModel?.setGenreIdsLiveData(AppUtil.provideSelectedGenresIntList(context!!)!!)
                    }
                }
            }
        } else {
            mInterestViewModel?.setGenreIdsLiveData(AppUtil.provideSelectedGenresIntList(context!!)!!)
        }

        (activity as NavigationActivity).setSupportActionBar(mBinding.toolbar)
        (activity as NavigationActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        initRecyclerView()

        subscribeToLiveData()

        subscribeToSharedPreferenceLiveData()
    }

    private fun initRecyclerView() {
        val adapter = InterestVerticalAdapter(mAppExecutors) { movie ->
            onQueueTransaction(MovieFragment.newInstance(
                    movie._id, movie.title!!, null, Traverse.INTEREST.value))
        }

        this.mAdapter = adapter

        mBinding.rvVerticalInterest.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        mBinding.rvVerticalInterest.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)

        mBinding.rvVerticalInterest.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mInterestViewModel?.mMoviesWithGenresLiveData?.observe(this, Observer {
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
                        mInterestViewModel?.initiateFetchInterestCall(AppUtil.provideSelectedGenresString(context!!)!!) { success ->
                            if (success) {
                                PreferenceUtil[activity!!, AppConstants.SharedPrefConstants.INTEREST_FETCH_COMPLETED] = true
                                mInterestViewModel?.setGenreIdsLiveData(AppUtil.provideSelectedGenresIntList(context!!)!!)
                            }
                        }
                    } else {
                        mInterestViewModel?.nukeInterestData()
                        mInterestViewModel?.setGenreIdsLiveData(emptyList())
                    }
                })
    }

    override fun onBackPressed(): Boolean {
        val layoutManager = mBinding.rvVerticalInterest.layoutManager as LinearLayoutManager
        return if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            onTraverseBack()
        } else {
            mBinding.rvVerticalInterest.smoothScrollToPosition(0)
            true
        }
    }

    override fun handleNavigationClick(): Boolean {
        val layoutManager = mBinding.rvVerticalInterest.layoutManager as LinearLayoutManager
        return if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            false
        } else {
            mBinding.rvVerticalInterest.smoothScrollToPosition(0)
            true
        }
    }
}
