package app.beer.kinozver.ui.fragments.show

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import app.beer.kinozver.R
import app.beer.kinozver.database.USER
import app.beer.kinozver.models.movie.Episode
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.utils.APP
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.AppRetrofitCallback
import app.beer.kinozver.utils.showToast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

class ShowMovieFragment : BaseFragment(R.layout.fragment_show_movie),
    BottomSheetEpisodes.EpisodesAdapter.OnEpisodeClickListener {

    var movieId: Int = 0
    var episode: Int = 0
    var seria: Int = 0
    var time: Int = 0

    lateinit var rotatePhoneBtn: Chip

    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnFullscreen: ImageView
    private lateinit var exoPlayer: SimpleExoPlayer

    private lateinit var playerRew: ImageView
    private lateinit var playerForward: ImageView

    // for the forward or rew on double click
    private lateinit var leftClick: FrameLayout
    private lateinit var rightClick: FrameLayout
    private var doubleLeftClickLastTime = 0L
    private var doubleRightClickLastTime = 0L
    private lateinit var minusTenTime: TextView
    private lateinit var plusTenTime: TextView

    private lateinit var backBtn: ImageView
    private lateinit var movieName: TextView
    private lateinit var playerVelocity: TextView
    private lateinit var playerEpisodes: TextView

    private var interstitialAd: InterstitialAd? = null

    // Is fullscreen
    private var flag = true

    private lateinit var document: Document

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = arguments?.getInt(EXTRA_MOVIE_ID) ?: 0
        episode = arguments?.getInt(EXTRA_MOVIE_EPISODE) ?: 0
        seria = arguments?.getInt(EXTRA_MOVIE_SERIA) ?: 0
        time = arguments?.getInt(EXTRA_MOVIE_TIME) ?: 0

        hideUI()

        with(view) {
            playerView = findViewById(R.id.player_view)
            progressBar = findViewById(R.id.progress_bar)

            btnFullscreen = findViewById(R.id.fullscreen_btn)
            playerRew = findViewById(R.id.player_rew)
            playerForward = findViewById(R.id.player_forward)
            leftClick = findViewById(R.id.left_click)
            rightClick = findViewById(R.id.right_click)
            minusTenTime = findViewById(R.id.minus_ten_time)
            plusTenTime = findViewById(R.id.plus_ten_time)

            backBtn = findViewById(R.id.back_btn)
            movieName = findViewById(R.id.movie_name)
            playerVelocity = findViewById(R.id.player_velocity)
            playerEpisodes = findViewById(R.id.player_episodes)

            rotatePhoneBtn = findViewById(R.id.rotate_phone_btn)
            rotatePhoneBtn.setOnClickListener {
                APP_ACTIVITY.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }

        playerVelocity.text = getString(R.string.velocity_label, "1x")
        progressBar.visibility = View.VISIBLE

        APP.getMovieApi().getMovieById(movieId, USER.id)
            .enqueue(AppRetrofitCallback<MovieJSON> { _, response ->
                val movie = response.body()
                if (movie != null) {
                    movieName.text = movie.title
                    if (movie.continueWatchTime != 0) time = movie.continueWatchTime
                    if (movie.continueEpisode != 0) episode = movie.continueEpisode
                    if (movie.continueSeria != 0) seria = movie.continueSeria
                    playerEpisodes.visibility =
                        if (movie.isSerial.toInt() == 1) View.VISIBLE else View.GONE
                    GlobalScope.launch(Dispatchers.IO) {
                        document =
                            Jsoup.connect("https://39.svetacdn.in/N3dMPlk0C1Dl?kp_id=${movie.kp_id}")
                                .followRedirects(true)
                                .get()
                        Log.d(
                            "ELEMENTS_FROM_PLAYER",
                            "${document.getElementsByAttribute("preload")}"
                        )
                        val url = document.getElementsByTag("video").attr("src")
                        APP_ACTIVITY.runOnUiThread { initPlayer("https://cloud.cdnland.in/tvseries/34466f9fec2a7b5e2c95e19e81fd631a9ef1f991/3217ef08d245186a80d374cf5d82e1f6:2021022820/240.mp4") }
                    }
                    progressBar.visibility = View.GONE
                }
            })

        backBtn.setOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
    }

    private lateinit var bottomSheetEpisodes: BottomSheetEpisodes

    @SuppressLint("ClickableViewAccessibility")
    private fun initPlayer(url: String) {
        loadAd()
        showToast(url)

        val videoUrl = Uri.parse(url)
        val loadControl = DefaultLoadControl()
        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelector = DefaultTrackSelector()
        // exoPlayer = SimpleExoPlayer.Builder(APP_ACTIVITY).build()
        exoPlayer = ExoPlayerFactory.newSimpleInstance(APP_ACTIVITY, trackSelector, loadControl)
        val factory = DefaultHttpDataSourceFactory("exoplayer_video")
        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource = ExtractorMediaSource(videoUrl, factory, extractorsFactory, null, null)
        playerView.player = exoPlayer
        playerView.keepScreenOn = true
        // exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = true
        exoPlayer.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {}

            override fun onLoadingChanged(isLoading: Boolean) {
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                // check condition
                if (playbackState == Player.STATE_BUFFERING) {
                    // show progress bar
                    progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    // hide progress bar
                    progressBar.visibility = View.GONE
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
        })

        if (time != 0) {
            exoPlayer.seekTo((time * 1000).toLong())
        }

        playerVelocity.setOnClickListener {
            val items = listOf("0,5x", "0,75x", "1x", "1,25x", "1,5x", "2x")
            val arrayAdapter = ArrayAdapter<String>(
                APP_ACTIVITY,
                android.R.layout.simple_list_item_1,
                items
            )
            val dialog = AlertDialog.Builder(APP_ACTIVITY)
                .setTitle(getString(R.string.choose_velocity))
                .setAdapter(arrayAdapter) { dialog, which ->
                    when (which) {
                        0 -> exoPlayer.setPlaybackParameters(PlaybackParameters(0.5f))
                        1 -> exoPlayer.setPlaybackParameters(PlaybackParameters(0.75f))
                        2 -> exoPlayer.setPlaybackParameters(PlaybackParameters(1f))
                        3 -> exoPlayer.setPlaybackParameters(PlaybackParameters(1.25f))
                        4 -> exoPlayer.setPlaybackParameters(PlaybackParameters(1.5f))
                        5 -> exoPlayer.setPlaybackParameters(PlaybackParameters(2f))
                    }
                    playerVelocity.text = getString(R.string.velocity_label, items[which])
                    dialog.dismiss()
                }.setNegativeButton(getString(R.string.cancel_label), null)
            dialog.show()
        }

        playerEpisodes.setOnClickListener {
            bottomSheetEpisodes = BottomSheetEpisodes()
            val adapter = BottomSheetEpisodes.EpisodesAdapter()
            val episodes = arrayListOf<Episode>()
            for (i in 0..27) {
                episodes.add(Episode("Name, $i", "Desc, $i", "image, $i", i))
            }
            adapter.setOnEpisodeClickListener(this)
            adapter.setEpisodes(episodes)
            bottomSheetEpisodes.setAdapter(adapter)
            bottomSheetEpisodes.show(APP_ACTIVITY.supportFragmentManager, "episodes_bottom_sheet")
        }

        btnFullscreen.setOnClickListener {
            if (flag) {
                btnFullscreen.setImageResource(R.drawable.ic_fullscreen_exit)
                APP_ACTIVITY.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                flag = false
            } else {
                btnFullscreen.setImageResource(R.drawable.ic_fullscreen)
                APP_ACTIVITY.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                flag = true
            }
        }

        playerForward.setOnClickListener {
            exoPlayer.seekTo(exoPlayer.currentPosition + 10000)
        }
        playerRew.setOnClickListener {
            exoPlayer.seekTo(exoPlayer.currentPosition - 10000)
        }

        leftClick.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - doubleLeftClickLastTime < 400) {
                    doubleLeftClickLastTime = 0L
                    exoPlayer.seekTo(exoPlayer.currentPosition - 10000)
                    minusTenTime.visibility = View.VISIBLE
                    minusTenTime.animate().translationX(0f).duration = 400
                    Handler().postDelayed({ minusTenTime.visibility = View.GONE }, 500)
                } else {
                    doubleLeftClickLastTime = System.currentTimeMillis()
                }
            }
            true
        }
        rightClick.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - doubleRightClickLastTime < 400) {
                    doubleRightClickLastTime = 0L
                    exoPlayer.seekTo(exoPlayer.currentPosition + 10000)
                    plusTenTime.visibility = View.VISIBLE
                    plusTenTime.animate().translationX(0f).duration = 400
                    Handler().postDelayed({ plusTenTime.visibility = View.GONE }, 700)
                } else {
                    doubleRightClickLastTime = System.currentTimeMillis()
                }
            }
            true
        }
    }

    override fun onEpisodeClick(episode: Episode) {
        showToast(episode.name)
        bottomSheetEpisodes.dismiss()
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.supportActionBar?.hide()
        APP_ACTIVITY.bottomNavigationView.visibility = View.GONE

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                APP_ACTIVITY.runOnUiThread { loadAd() }
            }
        }, 0, 10000) // 60000 * 35
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        rotatePhoneBtn.visibility =
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                btnFullscreen.setImageResource(R.drawable.ic_fullscreen)
                flag = true
                View.VISIBLE
            } else {
                btnFullscreen.setImageResource(R.drawable.ic_fullscreen_exit)
                flag = false
                View.GONE
            }
    }

    override fun onPause() {
        super.onPause()
        showSystemUI()
        APP_ACTIVITY.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // TODO: Сохранить время (или эпизод и серию если это сериал), отправив запрос в api
        exoPlayer.playWhenReady = false
        exoPlayer.playbackState
    }

    private fun loadAd() {
        interstitialAd = InterstitialAd(APP_ACTIVITY)
        interstitialAd!!.adUnitId = "ca-app-pub-1670601913858216/9772306793"
        interstitialAd!!.loadAd(AdRequest.Builder().build())
        interstitialAd!!.show()
        interstitialAd!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                exoPlayer.stop()
            }

            override fun onAdClosed() {
                exoPlayer.playWhenReady = true
                exoPlayer.playbackState
            }
        }
    }

    private fun hideUI() {
        val decorView = activity!!.window.decorView
        val uiOptions = decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = newUiOptions
    }

    private fun showSystemUI() {
        val decorView = activity!!.window.decorView
        val uiOptions = decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
        decorView.systemUiVisibility = newUiOptions
    }

    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        const val EXTRA_MOVIE_EPISODE = "EXTRA_MOVIE_EPISODE"
        const val EXTRA_MOVIE_SERIA = "EXTRA_MOVIE_SERIA"
        const val EXTRA_MOVIE_TIME = "EXTRA_MOVIE_TIME"

        fun newInstance(movie_id: Int, episode: Int = 0, seria: Int = 0, time: Int = 0): Fragment {
            val args = Bundle()
            args.putInt(EXTRA_MOVIE_ID, movie_id)
            args.putInt(EXTRA_MOVIE_EPISODE, episode)
            args.putInt(EXTRA_MOVIE_SERIA, seria)
            args.putInt(EXTRA_MOVIE_TIME, time)
            val fragment = ShowMovieFragment()
            fragment.arguments = args
            return fragment
        }

    }

}