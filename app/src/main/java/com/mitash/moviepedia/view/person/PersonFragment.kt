package com.mitash.moviepedia.view.person

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentPersonBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.common.infiniteviewpager.InfinitePagerAdapter
import com.mitash.moviepedia.view.movie.MovieFragment
import com.mitash.moviepedia.vo.AppConstants
import com.mitash.moviepedia.vo.Status
import com.mitash.moviepedia.vo.apiresult.PersonCredits
import com.mitash.moviepedia.vo.apiresult.PersonImage
import com.mitash.moviepedia.vo.navigationstack.Traverse
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class PersonFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "PersonFragment"
        private const val EXTRA_PERSON_ID = "extraPersonId"
        private const val EXTRA_PERSON_NAME = "extraPersonName"
        private const val EXTRA_TRAVERSE_TYPE = "extraTraverseType"

        private const val DELAY_MS: Long = 500
        private const val PERIOD_MS: Long = 5000

        fun newInstance(personId: Int, personTitle: String, traverseType: Int): PersonFragment {
            val fragment = PersonFragment()
            val args = Bundle()
            args.putInt(EXTRA_PERSON_ID, personId)
            args.putString(EXTRA_PERSON_NAME, personTitle)
            args.putInt(EXTRA_TRAVERSE_TYPE, traverseType)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private lateinit var mPersonViewModel: PersonViewModel

    private var mBinding by autoCleared<FragmentPersonBinding>()

    private var mImageAdapter by autoCleared<PersonBoundAdapter<PersonImage.Results>>()

    private var mCreditAdapter by autoCleared<PersonBoundAdapter<PersonCredits.Cast>>()

    private var mSelectedTraverse: Traverse = Traverse.DISCOVER

    private var mCurrentPage = 0

    private var mTimer: Timer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentPersonBinding>(
                inflater,
                R.layout.fragment_person,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPersonViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(PersonViewModel::class.java)

        if (null != arguments) {
            if (arguments!!.containsKey(EXTRA_PERSON_ID)) {
                mPersonViewModel.subscribeLiveData(arguments!!.getInt(EXTRA_PERSON_ID))

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

                if (arguments!!.containsKey(EXTRA_PERSON_NAME)) {
                    initToolBar(arguments!!.getString(EXTRA_PERSON_NAME))
                }

                initImagesRecyclerView()

                initCreditsRecyclerView()

                subscribeToLiveData()

                mPersonViewModel.setBooleanLiveData(true)
            }
        }
    }

    private fun initToolBar(personTitle: String) {
        mBinding.personName = personTitle
        (activity as AppCompatActivity).setSupportActionBar(mBinding.toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        //remove last fragment when back key pressed
        mBinding.toolbar.setNavigationOnClickListener {
            if (null != activity) {
                activity?.onBackPressed()
            }
        }

        mBinding.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    mBinding.collapsingToolbar.title = personTitle
                    isShow = true
                } else if (isShow) {
                    mBinding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun initImagesRecyclerView() {
        val adapter = PersonBoundAdapter<PersonImage.Results>(mAppExecutors, null)
        this.mImageAdapter = adapter

        mBinding.rvPersonImages.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mBinding.rvPersonImages.layoutManager = layoutManager
    }

    private fun initCreditsRecyclerView() {
        val adapter = PersonBoundAdapter<PersonCredits.Cast>(mAppExecutors) { credit ->
            if (credit.mediaType == AppConstants.SearchConstants.MEDIA_TYPE_MOVIE) {
                onQueueTransaction(MovieFragment.newInstance(credit._id, credit.title!!, null, Traverse.DISCOVER.value))
            }
        }
        this.mCreditAdapter = adapter

        mBinding.rvPersonCredits.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mBinding.rvPersonCredits.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mPersonViewModel.mDetailLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && null != it.data) {
                mBinding.personDetail = it.data
                mBinding.flowTvPerson.text = it.data.biography
                mBinding.flowTvPerson.setTextSize(40F)
                mBinding.flowTvPerson.textColor = ContextCompat.getColor(context!!, R.color.colorTextDarkWhite)
                mBinding.flowTvPerson.invalidate()
            }
        })

        mPersonViewModel.mImagesLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && null != it.data
                    && null != it.data.results && it.data.results!!.isNotEmpty()) {
                initImageViewPager(it.data.results!!)
                mImageAdapter.submitList(it.data.results)
            } else {
                mImageAdapter.submitList(emptyList<PersonImage.Results>())
            }
        })

        mPersonViewModel.mCreditsLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && null != it.data
                    && null != it.data.cast && it.data.cast!!.isNotEmpty()) {
                mCreditAdapter.submitList(it.data.cast?.sortedByDescending { credit ->
                    credit.popularity
                })
            } else {
                mCreditAdapter.submitList(emptyList<PersonCredits.Cast>())
            }
        })
    }

    private fun initImageViewPager(results: List<PersonImage.Results>) {
        val adapter = PersonImagePagerAdapter(this.context!!, results)

        // wrap pager to provide infinite paging with wrap-around
        val wrappedAdapter = InfinitePagerAdapter(adapter)

        // actually an InfiniteViewPager
        val viewPager = mBinding.vpPersonImage as ViewPager
        viewPager.adapter = wrappedAdapter

        val handler = Handler()
        val update = Runnable {
            if (mCurrentPage == Int.MAX_VALUE) {
                mCurrentPage = 0
            }
            viewPager.setCurrentItem(mCurrentPage++, true)
        }

        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (!isHidden) {
                    handler.post(update)
                }
            }
        }, DELAY_MS, PERIOD_MS)
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
