package app.beer.kinozver.ui.fragments.show

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.MediaController
import android.widget.Toast
import app.beer.kinozver.R
import app.beer.kinozver.utils.APP_ACTIVITY
import kotlinx.android.synthetic.main.fragment_show_movie.*

class ShowMovieFragment : Fragment() {

    var movieId: Int? = null
    var url: String = ""

    lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_movie, container, false)
    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY.main_toolbar.visibility = View.GONE
        APP_ACTIVITY.bottomNavigationView.visibility = View.GONE
        movieId = arguments?.getInt(EXTRA_MOVIE_ID)
        url =
            "http://39.svetacdn.in/N3dMPlk0C1Dl/movie/$movieId?api_token=ht74hfMgvYHQRZi2qTOfae4BIQSxkK7n"
        webView = web_view
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.settings.javaScriptEnabled = true
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