package com.mitash.moviepedia.vo.navigationstack

import android.support.v4.app.Fragment

/**
 * Created by Mitash Gaurh on 7/20/2018.
 */
data class StackTransaction(
        val fragment: Fragment,
        val traverse: Traverse
)