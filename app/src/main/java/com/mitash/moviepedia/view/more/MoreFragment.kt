package com.mitash.moviepedia.view.more


import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentMoreBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.AppUtil
import com.mitash.moviepedia.util.PreferenceUtil
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.interest.InterestEditFragment
import com.mitash.moviepedia.view.navigation.NavigationActivity
import com.mitash.moviepedia.vo.AppConstants
import com.mitash.moviepedia.vo.navigationstack.StackTransaction
import com.mitash.moviepedia.vo.navigationstack.Traverse
import com.mitash.moviepedia.vo.stringSetLiveData
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class MoreFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "MoreFragment"

        fun newInstance(): MoreFragment = MoreFragment()
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private lateinit var mMoreViewModel: MoreViewModel

    private var mBinding by autoCleared<FragmentMoreBinding>()

    private var mAdapter by autoCleared<MoreInterestAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentMoreBinding>(
                inflater,
                R.layout.fragment_more,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMoreViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(MoreViewModel::class.java)

        (activity as NavigationActivity).setSupportActionBar(mBinding.toolbar)
        (activity as NavigationActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val selectedGenreIntList = AppUtil.provideSelectedGenresIntList(context!!)
        if (null != selectedGenreIntList) {
            mMoreViewModel.setGenreIdsLiveData(selectedGenreIntList)
        }
        initRecyclerView()

        mBinding.btnEdit.setOnClickListener {
            onQueueTransaction(StackTransaction(InterestEditFragment.newInstance()
                    , Traverse.MORE))
        }

        subscribeToLiveData()

        subscribeToSharedPreferenceLiveData()
    }

    private fun initRecyclerView() {
        val adapter = MoreInterestAdapter(mAppExecutors)

        this.mAdapter = adapter

        mBinding.rvMoreGenre.adapter = adapter
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW

        mBinding.rvMoreGenre.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mMoreViewModel.mSelectedGenresLiveData.observe(this, Observer {
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
                    if (null != it) {
                        val selectedGenreIntList = AppUtil.provideSelectedGenresIntList(context!!)
                        if (null != selectedGenreIntList) {
                            mMoreViewModel.setGenreIdsLiveData(selectedGenreIntList)
                        } else {
                            mMoreViewModel.setGenreIdsLiveData(emptyList())
                        }
                    }
                })
    }


    override fun onBackPressed(): Boolean {
        return onTraverseBack()
    }
}
