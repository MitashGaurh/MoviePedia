package com.mitash.moviepedia.vo.navigationstack

import android.support.v4.app.Fragment
import java.util.*

/**
 * Created by Mitash Gaurh on 7/22/2018.
 */
data class StackHolder(
        val tabFragment: Fragment?,
        val stack: Stack<Fragment> = Stack()
)