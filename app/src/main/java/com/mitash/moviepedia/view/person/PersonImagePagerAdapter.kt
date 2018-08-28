package com.mitash.moviepedia.view.person

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.LayoutSlidingImageBinding
import com.mitash.moviepedia.vo.apiresult.PersonImage


/**
 * Created by Mitash Gaurh on 8/16/2018.
 */
class PersonImagePagerAdapter constructor() : PagerAdapter() {

    private lateinit var mInflater: LayoutInflater
    private lateinit var mImages: List<PersonImage.Results>

    constructor(context: Context, results: List<PersonImage.Results>) : this() {
        mInflater = LayoutInflater.from(context)
        mImages = results
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mImages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val layoutSlidingImageBinding: LayoutSlidingImageBinding = DataBindingUtil.inflate(
                this.mInflater,
                R.layout.layout_sliding_image,
                view,
                false
        )

        layoutSlidingImageBinding.result = mImages[position]
        view.addView(layoutSlidingImageBinding.root, 0)

        return layoutSlidingImageBinding.root
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}