package com.mitash.moviepedia.util

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mitash.moviepedia.db.entity.Discover
import com.mitash.moviepedia.vo.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Created by Mitash Gaurh on 7/23/2018.
 */
class AppUtil {
    companion object {
        private val mTimeout = TimeUnit.HOURS.toMillis(1)

        fun hideKeyboard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(view: View) {
            view.requestFocus()
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }

        @Synchronized
        fun shouldTriggerSync(context: Context, isForced: Boolean): Boolean {
            val lastFetched = PreferenceUtil[context, AppConstants.SharedPrefConstants.LAST_SETUP_FETCH, 0.toLong()]
            val now = now()
            if (0.toLong() == lastFetched) {
                PreferenceUtil[context, AppConstants.SharedPrefConstants.LAST_SETUP_FETCH] = now
                return true
            }
            if (now - lastFetched > mTimeout) {
                PreferenceUtil[context, AppConstants.SharedPrefConstants.LAST_SETUP_FETCH] = now
                return true
            }
            if (isForced) {
                PreferenceUtil[context, AppConstants.SharedPrefConstants.LAST_SETUP_FETCH] = now
                return true
            }
            return false
        }

        private fun now() = SystemClock.uptimeMillis()

        fun buildDiscoverHeaders(context: Context): List<Discover> {
            val discoverList: ArrayList<Discover> = ArrayList()
            discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_TRENDING, "Trending Now"))
            discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_NOW_PLAYING, "In Cinema"))
            discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_MOST_POPULAR, "Most Popular Movies"))
            discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_UPCOMING, "Coming Soon"))
            if (PreferenceUtil.contains(context, AppConstants.SharedPrefConstants.SELECTED_GENRES)
                    && PreferenceUtil.getStringSetPreferences(context, AppConstants.SharedPrefConstants.SELECTED_GENRES).isNotEmpty()) {
                discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_GENRE_INTEREST_MOVIES, "Top picks for you"))
            }
            discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_TOP_RATED, "Top Rated Movies"))
            discoverList.add(Discover(AppConstants.SetupConstants.FETCH_TYPE_BEST_FROM_YEAR, "Best movies of the decade"))
            return discoverList
        }

        fun provideSelectedGenresIntList(context: Context): List<Int>? {
            val selectedGenresMap = PreferenceUtil.getStringSetPreferences(context, AppConstants.SharedPrefConstants.SELECTED_GENRES)
            if (selectedGenresMap.isNotEmpty()) {
                val selectedGenreStringBuffer = StringBuffer(selectedGenresMap.toString())
                selectedGenreStringBuffer.deleteCharAt(0)
                selectedGenreStringBuffer.deleteCharAt(selectedGenreStringBuffer.lastIndex)

                val selectedGenreString = selectedGenreStringBuffer.toString().replace(", ", "|")
                val selectedGenresList = selectedGenreString.split("|")
                val selectedGenreIntList = ArrayList<Int>()
                selectedGenresList.forEach { selectedGenre ->
                    selectedGenreIntList.add(selectedGenre.toInt())
                }
                return selectedGenreIntList
            }
            return null
        }

        fun provideSelectedGenresString(context: Context): String? {
            val selectedGenresMap = PreferenceUtil.getStringSetPreferences(context, AppConstants.SharedPrefConstants.SELECTED_GENRES)
            if (selectedGenresMap.isNotEmpty()) {
                val selectedGenreStringBuffer = StringBuffer(selectedGenresMap.toString())
                selectedGenreStringBuffer.deleteCharAt(0)
                selectedGenreStringBuffer.deleteCharAt(selectedGenreStringBuffer.lastIndex)

                return selectedGenreStringBuffer.toString().replace(", ", "|")
            }
            return null
        }
    }
}