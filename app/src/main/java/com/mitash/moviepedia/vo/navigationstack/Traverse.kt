package com.mitash.moviepedia.vo.navigationstack

import com.mitash.moviepedia.R

/**
 * Created by Mitash Gaurh on 7/20/2018.
 */
enum class Traverse(var value: Int) {
    DISCOVER(R.id.action_discover),
    SEARCH(R.id.action_search),
    INTEREST(R.id.action_interest),
    MORE(R.id.action_more);

    override fun toString(): String {
        return this.value.toString()
    }
}