package com.mitash.moviepedia.view.search

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentSearchBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.AppUtil
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.movie.MovieFragment
import com.mitash.moviepedia.view.person.PersonFragment
import com.mitash.moviepedia.vo.AppConstants
import com.mitash.moviepedia.vo.navigationstack.StackTransaction
import com.mitash.moviepedia.vo.navigationstack.Traverse
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "SearchFragment"

        fun newInstance(): SearchFragment = SearchFragment()
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mSearchViewModel: SearchViewModel

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private var mBinding by autoCleared<FragmentSearchBinding>()

    private var mAdapter by autoCleared<SearchAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentSearchBinding>(
                inflater,
                R.layout.fragment_search,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSearchViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(SearchViewModel::class.java)

        mSearchViewModel.clearSearchTable()

        initSearchView()

        initRecyclerView()

        subscribeToLiveData()
    }

    private fun initSearchView() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mBinding.svSearch.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        mBinding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                AppUtil.hideKeyboard(mBinding.svSearch)
                mBinding.svSearch.clearFocus()
                mSearchViewModel.setQuery(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mSearchViewModel.setQuery(newText!!)
                return true
            }
        })
    }

    private fun initRecyclerView() {
        val adapter = SearchAdapter(mAppExecutors, this.activity!!) { search ->
            if (search.mediaType == AppConstants.SearchConstants.MEDIA_TYPE_MOVIE) {
                onQueueTransaction(StackTransaction(
                        MovieFragment.newInstance(search._id, search.title!!, null, Traverse.SEARCH.value)
                        , Traverse.SEARCH))
            } else if (search.mediaType == AppConstants.SearchConstants.MEDIA_TYPE_PERSON) {
                onQueueTransaction(StackTransaction(PersonFragment.newInstance(
                        search._id, search.name!!, Traverse.SEARCH.value)
                        , Traverse.SEARCH))
            }
        }
        this.mAdapter = adapter

        mBinding.rvSearch.adapter = adapter
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START

        mBinding.rvSearch.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mSearchViewModel.mSearchLiveData.observe(this, Observer {
            if (it?.data != null && it.data.isNotEmpty()) {
                mAdapter.submitList(it.data)
            } else {
                mAdapter.submitList(emptyList())
            }
        })
    }

    override fun onBackPressed(): Boolean {
        if (mBinding.svSearch.query.isNotEmpty()) {
            mBinding.svSearch.setQuery("", false)
            mSearchViewModel.clearSearchTable()
            return true
        }
        return onTraverseBack()
    }
}