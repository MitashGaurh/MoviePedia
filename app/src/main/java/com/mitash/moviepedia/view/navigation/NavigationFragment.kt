package com.mitash.moviepedia.view.navigation

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.R
import com.mitash.moviepedia.di.Injectable
import javax.inject.Inject

class NavigationFragment : Fragment(), Injectable {

    companion object {
        @JvmStatic
        fun newInstance() = NavigationFragment()
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mNavigationViewModel: NavigationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mNavigationViewModel = ViewModelProviders.of(requireActivity(), mViewModelFactory)
                .get(NavigationViewModel::class.java)

        mNavigationViewModel.initFragmentManager(childFragmentManager)

        mNavigationViewModel.performBottomTabFragmentsTransaction(R.id.action_discover)

    }
}
