package com.mitash.moviepedia.view.movie


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentMovieBinding
import com.mitash.moviepedia.db.entity.Cast
import com.mitash.moviepedia.db.entity.Video
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.collection.CollectionFragment
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.discover.DiscoverHorizontalAdapter
import com.mitash.moviepedia.view.person.PersonFragment
import com.mitash.moviepedia.view.youtube.YoutubeActivity
import com.mitash.moviepedia.vo.Status
import com.mitash.moviepedia.vo.apiresult.MovieDetail
import com.mitash.moviepedia.vo.navigationstack.StackTransaction
import com.mitash.moviepedia.vo.navigationstack.Traverse
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class MovieFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "MovieFragment"
        private const val EXTRA_MOVIE_ID = "extraMovieId"
        private const val EXTRA_MOVIE_TITLE = "extraMovieTitle"
        private const val EXTRA_FETCH_TYPE = "extraFetchType"
        private const val EXTRA_TRAVERSE_TYPE = "extraTraverseType"

        fun newInstance(movieId: Int, movieTitle: String, fetchType: String?, traverseType: Int): MovieFragment {
            val fragment = MovieFragment()
            val args = Bundle()
            args.putInt(EXTRA_MOVIE_ID, movieId)
            args.putString(EXTRA_MOVIE_TITLE, movieTitle)
            if (null != fetchType) {
                args.putString(EXTRA_FETCH_TYPE, fetchType)
            }
            args.putInt(EXTRA_TRAVERSE_TYPE, traverseType)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private lateinit var mMovieViewModel: MovieViewModel

    private var mBinding by autoCleared<FragmentMovieBinding>()

    private var mVideoAdapter by autoCleared<MovieBoundAdapter<Video>>()

    private var mCastAdapter by autoCleared<MovieBoundAdapter<Cast>>()

    private var mSimilarAdapter by autoCleared<DiscoverHorizontalAdapter>()

    private var mSelectedTraverse: Traverse = Traverse.DISCOVER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentMovieBinding>(
                inflater,
                R.layout.fragment_movie,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMovieViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(MovieViewModel::class.java)

        if (null != arguments) {
            if (arguments!!.containsKey(EXTRA_MOVIE_ID)) {
                if (arguments!!.containsKey(EXTRA_FETCH_TYPE)) {
                    mMovieViewModel.subscribeLocalMovieLiveData(arguments!!.getInt(EXTRA_MOVIE_ID)
                            , arguments!!.getString(EXTRA_FETCH_TYPE))
                } else {
                    mMovieViewModel.subscribeRemoteMovieLiveData(arguments!!.getInt(EXTRA_MOVIE_ID))
                }

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

                mMovieViewModel.setBooleanLiveData(true)
                if (arguments!!.containsKey(EXTRA_MOVIE_TITLE)) {
                    initToolBar(arguments!!.getString(EXTRA_MOVIE_TITLE))
                }
            }
        }

        initVideosRecyclerView()

        initCastsRecyclerView()

        initSimilarRecyclerView()

        subscribeToLiveData()
    }

    private fun initToolBar(movieTitle: String) {
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
                    mBinding.collapsingToolbar.title = movieTitle
                    isShow = true
                } else if (isShow) {
                    mBinding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun initVideosRecyclerView() {
        val adapter = MovieBoundAdapter<Video>(mAppExecutors)
        this.mVideoAdapter = adapter

        mBinding.rvMovieVideos.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mBinding.rvMovieVideos.layoutManager = layoutManager
    }

    private fun initCastsRecyclerView() {
        val adapter = MovieBoundAdapter<Cast>(mAppExecutors)
        this.mCastAdapter = adapter

        mBinding.rvMovieCast.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mBinding.rvMovieCast.layoutManager = layoutManager
    }

    private fun initSimilarRecyclerView() {
        val adapter = DiscoverHorizontalAdapter(mAppExecutors) { movie ->
            when (mSelectedTraverse) {
                Traverse.DISCOVER -> onQueueTransaction(StackTransaction(newInstance(
                        movie._id, movie.title!!, null, Traverse.DISCOVER.value)
                        , Traverse.DISCOVER))
                Traverse.SEARCH -> onQueueTransaction(StackTransaction(newInstance(
                        movie._id, movie.title!!, null, Traverse.SEARCH.value)
                        , Traverse.SEARCH))
                Traverse.INTEREST -> onQueueTransaction(StackTransaction(newInstance(
                        movie._id, movie.title!!, null, Traverse.INTEREST.value)
                        , Traverse.INTEREST))
                else -> {
                    throw IllegalArgumentException("Traverse not found")
                }
            }
        }
        this.mSimilarAdapter = adapter

        mBinding.rvMovieSimilar.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        mBinding.rvMovieSimilar.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        if (mSelectedTraverse == Traverse.DISCOVER || mSelectedTraverse == Traverse.INTEREST) {
            mMovieViewModel.mMovieLiveData?.observe(this, Observer {
                if (null != it) {
                    mBinding.movie = it
                    mBinding.flowTvMovie.text = it.overview
                    mBinding.flowTvMovie.setTextSize(40F)
                    mBinding.flowTvMovie.textColor = ContextCompat.getColor(context!!, R.color.colorTextDarkWhite)
                    mBinding.flowTvMovie.invalidate()
                }
            })
        }

        mMovieViewModel.mDetailLiveData.observe(this, Observer {
            if (null != it?.data) {
                mBinding.movieDetail = it.data
                if (mBinding.movie == null) {
                    mBinding.flowTvMovie.text = it.data.overview
                    mBinding.flowTvMovie.setTextSize(40F)
                    mBinding.flowTvMovie.textColor = ContextCompat.getColor(context!!, R.color.colorTextDarkWhite)
                    mBinding.flowTvMovie.invalidate()
                }

                if (null != it.data.belongsToCollection) {
                    setUpCollectionClick(it.data.belongsToCollection)
                }

                val genresStringBuilder = StringBuilder()
                if (it.data.genres?.size == 1) {
                    genresStringBuilder.append(it.data.genres[0].name)
                } else {
                    val size = if (it.data.genres?.size!! > 3) {
                        3
                    } else {
                        it.data.genres.size
                    }
                    for (i in 0 until size) {
                        if (i == size - 1) {
                            genresStringBuilder.append(it.data.genres[i].name)
                        } else {
                            genresStringBuilder.append(it.data.genres[i].name + ", ")
                        }
                    }
                }
                mBinding.genres = genresStringBuilder.toString()

                val countriesStringBuilder = StringBuilder()
                if (it.data.productionCountries?.size == 1) {
                    countriesStringBuilder.append(it.data.productionCountries[0].name)
                } else {
                    for (i in 0 until it.data.productionCountries?.size!!) {
                        if (i == it.data.productionCountries.size - 1) {
                            countriesStringBuilder.append("and " + it.data.productionCountries[i].name)
                        } else {
                            countriesStringBuilder.append(it.data.productionCountries[i].name + ", ")
                        }
                    }
                }
                mBinding.productionCountries = countriesStringBuilder.toString()

                val languageStringBuilder = StringBuilder()
                if (it.data.spokenLanguages?.size == 1) {
                    languageStringBuilder.append(it.data.spokenLanguages[0].name)
                } else {
                    for (i in 0 until it.data.spokenLanguages?.size!!) {
                        if (i == it.data.spokenLanguages.size - 1) {
                            languageStringBuilder.append("and " + it.data.spokenLanguages[i].name)
                        } else {
                            languageStringBuilder.append(it.data.spokenLanguages[i].name + ", ")
                        }
                    }
                }
                mBinding.spokenLanguage = languageStringBuilder.toString()
            }
        })

        mMovieViewModel.mVideosLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                mBinding.thumbnail = it.data[0].key
                setupExpandedImage(it.data[0].key!!)
                mBinding.isVideoMode = true
                val youtubeIdList = ArrayList<String>()
                it.data.forEach { video ->
                    youtubeIdList.add(video.key!!)
                }

                mVideoAdapter.submitList(it.data)
                mVideoAdapter.setItemClickListener(object : MovieBoundAdapter.ItemClickListener {
                    override fun onVideoItemClick(item: Video) {
                        startVideoPlaylist(item.key!!, youtubeIdList)
                    }

                    override fun onCastItemClick(item: Cast) {
                    }
                })
            } else {
                mBinding.isVideoMode = false
                mVideoAdapter.submitList(emptyList<Video>())
            }
        })

        mMovieViewModel.mCastsLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                mCastAdapter.submitList(it.data)
                mCastAdapter.setItemClickListener(object : MovieBoundAdapter.ItemClickListener {
                    override fun onVideoItemClick(item: Video) {
                    }

                    override fun onCastItemClick(item: Cast) {
                        when (mSelectedTraverse) {
                            Traverse.DISCOVER -> onQueueTransaction(StackTransaction(PersonFragment.newInstance(
                                    item.id!!, item.name!!, Traverse.DISCOVER.value)
                                    ,
                                    Traverse.DISCOVER))
                            Traverse.SEARCH -> onQueueTransaction(StackTransaction(PersonFragment.newInstance(
                                    item.id!!, item.name!!, Traverse.SEARCH.value)
                                    ,
                                    Traverse.SEARCH))
                            Traverse.INTEREST -> onQueueTransaction(StackTransaction(PersonFragment.newInstance(
                                    item.id!!, item.name!!, Traverse.INTEREST.value)
                                    ,
                                    Traverse.INTEREST))
                            else -> {
                                throw IllegalArgumentException("Traverse not found")
                            }
                        }
                    }

                })
            } else {
                mCastAdapter.submitList(emptyList<Cast>())
            }
        })

        mMovieViewModel.mCrewLiveData.observe(this, Observer {
            if (null != it && it.isNotEmpty()) {
                val director = it.filter { crew ->
                    crew.job == "Director"
                }
                val writers = it.filter { crew ->
                    crew.job == "Screenplay"
                }
                mBinding.director = director[0].name
                val writersStringBuilder = StringBuilder()
                if (writers.size == 1) {
                    writersStringBuilder.append(writers[0].name)
                } else {
                    for (i in 0 until writers.size) {
                        if (i == writers.size - 1) {
                            writersStringBuilder.append("and " + writers[i].name)
                        } else {
                            writersStringBuilder.append(writers[i].name + ", ")
                        }
                    }
                }
                mBinding.writers = writersStringBuilder.toString()
            }
        })

        mMovieViewModel.mSimilarMoviesLiveData.observe(this, Observer {
            if (it?.status == Status.SUCCESS && it.data!!.isNotEmpty()) {
                mBinding.isSimilarMode = true
                mSimilarAdapter.submitList(it.data)
            } else {
                mBinding.isSimilarMode = false
                mSimilarAdapter.submitList(emptyList())
            }
        })
    }

    private fun setUpCollectionClick(belongsToCollection: MovieDetail.BelongsToCollection) {
        mBinding.cvCollection.setOnClickListener {
            when (mSelectedTraverse) {
                Traverse.DISCOVER -> onQueueTransaction(StackTransaction(CollectionFragment.newInstance(
                        belongsToCollection._id!!, belongsToCollection.name!!, Traverse.DISCOVER.value),
                        Traverse.DISCOVER))
                Traverse.SEARCH -> onQueueTransaction(StackTransaction(CollectionFragment.newInstance(
                        belongsToCollection._id!!, belongsToCollection.name!!, Traverse.SEARCH.value),
                        Traverse.SEARCH))
                Traverse.INTEREST -> onQueueTransaction(StackTransaction(CollectionFragment.newInstance(
                        belongsToCollection._id!!, belongsToCollection.name!!, Traverse.INTEREST.value),
                        Traverse.INTEREST))
                else -> {
                    throw IllegalArgumentException("Traverse not found")
                }
            }
        }
    }

    private fun setupExpandedImage(youtubeId: String) {
        mBinding.expandedImage.setOnClickListener {
            val intent = Intent(activity, YoutubeActivity::class.java)
            intent.putExtra(YoutubeActivity.EXTRA_YOUTUBE_ID, youtubeId)
            startActivity(intent)
        }
    }

    private fun startVideoPlaylist(youtubeId: String, youtubeIds: ArrayList<String>) {
        val intent = Intent(activity, YoutubeActivity::class.java)
        intent.putExtra(YoutubeActivity.EXTRA_YOUTUBE_ID, youtubeId)
        intent.putStringArrayListExtra(YoutubeActivity.EXTRA_YOUTUBE_ID_LIST, youtubeIds)
        startActivity(intent)
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