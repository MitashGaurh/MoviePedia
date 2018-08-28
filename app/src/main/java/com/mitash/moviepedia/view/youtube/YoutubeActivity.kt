package com.mitash.moviepedia.view.youtube

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.mitash.moviepedia.R
import com.mitash.moviepedia.databinding.ActivityYoutubeBinding
import com.mitash.moviepedia.vo.AppConstants


class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    companion object {
        private const val RECOVERY_DIALOG_REQUEST = 1
        const val EXTRA_YOUTUBE_ID = "extraYoutubeId"
        const val EXTRA_YOUTUBE_ID_LIST = "extraYoutubeIdList"
    }

    private var mActivityYoutubeBinding: ActivityYoutubeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Remove notification bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        mActivityYoutubeBinding = DataBindingUtil.setContentView(this, R.layout.activity_youtube)

        mActivityYoutubeBinding?.youtubePlayer?.initialize(AppConstants.YOUTUBE_API_KEY, this)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        player?.setShowFullscreenButton(false)
        player?.setFullscreen(true)
        if (!wasRestored && intent.hasExtra(EXTRA_YOUTUBE_ID)) {
            if (intent.hasExtra(EXTRA_YOUTUBE_ID_LIST)) {
                player?.cueVideos(intent.getStringArrayListExtra(EXTRA_YOUTUBE_ID_LIST)
                        , intent.getStringArrayListExtra(EXTRA_YOUTUBE_ID_LIST)
                        .indexOf(intent.getStringExtra(EXTRA_YOUTUBE_ID)), 0)
            } else {
                player?.cueVideo(intent.getStringExtra(EXTRA_YOUTUBE_ID))
            }
            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult?) {
        if (errorReason?.isUserRecoverableError!!) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(getString(R.string.error_player), errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            mActivityYoutubeBinding?.youtubePlayer?.initialize(AppConstants.YOUTUBE_API_KEY, this)
        }
    }
}
