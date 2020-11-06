package app.beer.kinozver.ui.fragments.show

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.*
import app.beer.kinozver.R
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.utils.APP_ACTIVITY
import kotlinx.android.synthetic.main.fragment_show_movie.*

class ShowMovieFragment : BaseFragment(R.layout.fragment_show_movie) {

    var movieId: Int? = null
    var url: String = ""

    lateinit var webView: WebView

    override fun onStart() {
        super.onStart()

        // APP_ACTIVITY.main_toolbar.visibility = View.GONE
        APP_ACTIVITY.supportActionBar?.hide()
        APP_ACTIVITY.bottomNavigationView.visibility = View.GONE
        movieId = arguments?.getInt(EXTRA_MOVIE_ID)
        url =
            "https://39.svetacdn.in/N3dMPlk0C1Dl/movie/$movieId?api_token=ht74hfMgvYHQRZi2qTOfae4BIQSxkK7n"
        webView = web_view
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setAppCacheEnabled(true)
        webView.loadUrl(url)
    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?
        ): Boolean {
            view?.loadUrl(url)
            return true
        }
    }

    class MyWebChromeClient : WebChromeClient() {

    }

    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun newInstance(movie_id: Int): Fragment {
            val args = Bundle()
            args.putInt(EXTRA_MOVIE_ID, movie_id)
            val fragment = ShowMovieFragment()
            fragment.arguments = args
            return fragment
        }

    }

}