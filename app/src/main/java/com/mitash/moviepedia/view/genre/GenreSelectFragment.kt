package com.mitash.moviepedia.view.genre


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mitash.moviepedia.AppExecutors
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentGenreSelectBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.AppUtil
import com.mitash.moviepedia.util.PreferenceUtil
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.navigation.NavigationActivity
import com.mitash.moviepedia.vo.AppConstants
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class GenreSelectFragment : Fragment(), Injectable {

    companion object {
        private const val TAG = "DiscoverFragment"
        private const val EXTRA_IS_EDIT_MODE = "extraIsEditMode"

        fun newInstance(isEditMode: Boolean): GenreSelectFragment {
            val fragment = GenreSelectFragment()
            val args = Bundle()
            args.putBoolean(EXTRA_IS_EDIT_MODE, isEditMode)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mGenreViewModel: GenreViewModel

    @Inject
    lateinit var mAppExecutors: AppExecutors

    private var mBinding by autoCleared<FragmentGenreSelectBinding>()

    private var mAdapter by autoCleared<GenreSelectAdapter>()

    private var isEditMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentGenreSelectBinding>(
                inflater,
                R.layout.fragment_genre_select,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mGenreViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(GenreViewModel::class.java)

        if (null != arguments) {
            if (arguments!!.containsKey(EXTRA_IS_EDIT_MODE)) {
                isEditMode = arguments!!.getBoolean(EXTRA_IS_EDIT_MODE)
                mBinding.isEditMode = isEditMode
            }
        }
        mGenreViewModel.setBooleanLiveData(true)

        initRecyclerView()

        subscribeToLiveData()

        mBinding.btnDone.setOnClickListener {
            PreferenceUtil[context!!, AppConstants.SharedPrefConstants.SELECTED_GENRES] = mAdapter.mGenreSet
            if (isEditMode) {
                PreferenceUtil[context!!, AppConstants.SharedPrefConstants.INTEREST_FETCH_COMPLETED] = false
            }
            if (!isEditMode) {
                activity?.startActivity(Intent(activity, NavigationActivity::class.java))
                activity?.finish()
            } else {
                activity?.onBackPressed()
            }
        }
    }

    private fun initRecyclerView() {
        val adapter = GenreSelectAdapter(mAppExecutors)
        this.mAdapter = adapter

        mBinding.rvGenreSelect.adapter = adapter
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER

        mBinding.rvGenreSelect.layoutManager = layoutManager
    }

    private fun subscribeToLiveData() {
        mGenreViewModel.mGenreLiveData.observe(this, Observer {
            if (null != it && it.isNotEmpty()) {
                if (isEditMode) {
                    val selectedGenreList = AppUtil.provideSelectedGenresIntList(context!!)
                    if (null != selectedGenreList) {
                        it.forEach { genre ->
                            if (selectedGenreList.contains(genre._id)) {
                                genre.isSelected = true
                                mAdapter.mGenreSet.add(genre._id.toString())
                            }
                        }
                    }
                }
                mAdapter.submitList(it)
            } else {
                mAdapter.submitList(emptyList())
            }
        })
    }
}
