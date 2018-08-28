package com.mitash.moviepedia.binding

import android.databinding.BindingAdapter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mitash.moviepedia.R
import com.mitash.moviepedia.util.ConnectionUtils


/**
 * Created by Mitash Gaurh on 7/18/2018.
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("stringToDate")
    fun releaseDateFormat(textView: TextView, dateString: String) {
        if (dateString.isNotEmpty()) {
            val yearString = dateString.substringBefore("-") + "  "
            textView.text = yearString
        }
    }

    @JvmStatic
    @BindingAdapter("durationToString")
    fun durationString(textView: TextView, duration: Int) {
        if (duration != 0) {
            val hours = duration / 60
            val min = duration % 60
            val durationString = hours.toString() + "h" + min.toString() + "min" + "  "
            textView.text = durationString
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        if (null != url && url.isNotEmpty()) {
            val circularProgressDrawable = CircularProgressDrawable(imageView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(imageView.context, R.color.colorTextDarkWhite), PorterDuff.Mode.SRC)
            circularProgressDrawable.start()
            Glide.with(imageView.context).load("https://image.tmdb.org/t/p/w500$url")
                    .apply(RequestOptions()
                            .placeholder(circularProgressDrawable))
                    .into(imageView)
        } else {
            imageView.setImageDrawable(null)
        }
    }

    @JvmStatic
    @BindingAdapter("youtubeThumbnailUrl")
    fun bindThumbnailImage(imageView: ImageView, youtubeVideoId: String?) {
        if (null != youtubeVideoId && youtubeVideoId.isNotEmpty()) {
            val netState = ConnectionUtils.isConnected(imageView.context)
            if (netState == ConnectionUtils.NetState.NET_2G) {
                Glide.with(imageView.context).load("https://img.youtube.com/vi/$youtubeVideoId/default.jpg")
                        .into(imageView)
            } else if (netState == ConnectionUtils.NetState.NET_3G) {
                Glide.with(imageView.context).load("https://img.youtube.com/vi/$youtubeVideoId/mqdefault.jpg")
                        .into(imageView)
            } else if (netState == ConnectionUtils.NetState.NET_4G
                    || netState == ConnectionUtils.NetState.NET_WIFI) {
                Glide.with(imageView.context).load("https://img.youtube.com/vi/$youtubeVideoId/hqdefault.jpg")
                        .into(imageView)
            }
        } else {
            imageView.setImageDrawable(null)
        }
    }

    @JvmStatic
    @BindingAdapter("textFlex")
    fun bindFlex(textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            textView.setBackgroundResource(R.drawable.bg_tag_selected)
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorPrimaryDark))
        } else {
            textView.setBackgroundResource(R.drawable.bg_tag)
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorAccent))
        }
    }
}