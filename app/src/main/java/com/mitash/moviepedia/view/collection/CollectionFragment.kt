package com.mitash.moviepedia.view.collection

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentCollectionBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.movie.MovieFragment
import com.mitash.moviepedia.vo.Status
import com.mitash.moviepedia.vo.navigationstack.Traverse
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class CollectionFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "CollectionFragment"
        private const val EXTRA_COLLECTION_ID = "extraCollectionId"
        private const val EXTRA_COLLECTION_NAME = "extraCollectionName"
        private const val EXTRA_TRAVERSE_TYPE = "extraTraverseType"

        fun newInstance(collectionId: Int, collectionName: String, traverseType: Int): CollectionFragment {
            val fragment = CollectionFragment()
            val args = Bundle()
            args.putInt(EXTRA_COLLECTION_ID, collectionId)
            args.putString(EXTRA_COLLECTION_NAME, collectionName)
            args.putInt(EXTRA_TRAVERSE_TYPE, traverseType)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private lateinit var mCollectionViewModel: CollectionViewModel

    private var mBinding by autoCleared<FragmentCollectionBinding>()

    private var mPartsAdapter by autoCleared<CollectionPartsAdapter>()

    private var mSelectedTraverse: Traverse = Traverse.DISCOVER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentCollectionBinding>(
                inflater,
                R.layout.fragment_collection,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mCollectionViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(CollectionViewModel::class.java)

        if (null != arguments) {
            if (arguments!!.containsKey(EXTRA_TRAVERSE_TYPE)) {
                when {
                    arguments!!.getInt(EXTRA_TRAVERSE_TYPE) == Traverse.DISCOVER.value ->
                        mSelectedTraverse = Traverse.DISCOVER
                    arguments!!.getInt(EXTRA_TRAVERSE_TYPE) == Traverse.SEARCH.value ->
                        mSelectedTraverse = Traverse.SEARCH
                    arguments!!.getInt(EXTRA_TRAVERSE_TYPE) == Traverse.INTEREST.value ->
                        mSelectedTraverse = Traverse.INTEREST
                }
            }

            if (arguments!!.containsKey(EXTRA_COLLECTION_NAME)) {
                initToolBar(arguments!!.getString(EXTRA_COLLECTION_NAME)!!)
            }

            if (arguments!!.containsKey(EXTRA_COLLECTION_ID)) {
                mCollectionViewModel.subscribeLiveData(arguments!!.getInt(EXTRA_COLLECTION_ID))
                mCollectionViewModel.setBooleanLiveData(true)
            }

            initPartsRecyclerView()

            subscribeToLiveData()
        }
    }

    private fun initToolBar(collectionName: String) {
        mBinding.collectionName = collectionName
        (activity as AppCompatActivity).setSupportActionBar(mBinding.toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.title = collectionName

        //remove last fragment when back key pressed
        mBinding.toolbar.setNavigationOnClickListener {
            if (null != activity) {
                activity?.onBackPressed()
            }
        }
    }

    private fun initPartsRecyclerView() {
        val adapter = CollectionPartsAdapter(mAppExecutors) { part ->
            onQueueTransaction(MovieFragment.newInstance(
                    part._id, part.title!!, null, Traverse.DISCOVER.value))
        }

        this.mPartsAdapter = adapter

        mBinding.rvCollectionParts.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mBinding.rvCollectionParts.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mCollectionViewModel.mDetailLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && null != it.data) {
                mBinding.collectionDetail = it.data
                mBinding.flowTvCollection.text = it.data.overview
                mBinding.flowTvCollection.setTextSize(40F)
                mBinding.flowTvCollection.textColor = ContextCompat.getColor(context!!, R.color.colorTextDarkWhite)
                mBinding.flowTvCollection.invalidate()
                if (null != it.data.parts && it.data.parts?.isNotEmpty()!!) {
                    mPartsAdapter.submitList(it.data.parts)
                } else {
                    mPartsAdapter.submitList(emptyList())
                }
            }
        })
    }

    override fun onBackPressed(): Boolean {
        return when (mSelectedTraverse) {
            Traverse.DISCOVER -> onTraverseBack()
            Traverse.SEARCH -> onTraverseBack()
            Traverse.INTEREST -> onTraverseBack()
            else -> {
                throw IllegalArgumentException("Traverse not found")
            }
        }
    }
}