package com.mitash.moviepedia.view.interest


import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.FragmentInterestEditBinding
import com.mitash.moviepedia.di.Injectable
import com.mitash.moviepedia.util.ActivityUtils
import com.mitash.moviepedia.util.autoCleared
import com.mitash.moviepedia.view.common.BackHandledFragment
import com.mitash.moviepedia.view.genre.GenreSelectFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class InterestEditFragment : BackHandledFragment(), Injectable {

    companion object {
        private const val TAG = "InterestEditFragment"

        fun newInstance(): InterestEditFragment = InterestEditFragment()
    }

    private var mBinding by autoCleared<FragmentInterestEditBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentInterestEditBinding>(
                inflater,
                R.layout.fragment_interest_edit,
                container,
                false
        )

        mBinding = dataBinding

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolBar()

        ActivityUtils.addFragmentToActivity(childFragmentManager
                , GenreSelectFragment.newInstance(true)
                , R.id.interest_edit_container
                , false
                , GenreSelectFragment::class.simpleName)
    }

    private fun initToolBar() {
        (activity as AppCompatActivity).setSupportActionBar(mBinding.toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.title = "Select your Interests."

        //remove last fragment when back key pressed
        mBinding.toolbar.setNavigationOnClickListener {
            if (null != activity) {
                activity?.onBackPressed()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return onTraverseBack()
    }

}
