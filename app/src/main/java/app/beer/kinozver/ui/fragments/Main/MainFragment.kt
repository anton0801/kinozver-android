package app.beer.kinozver.ui.fragments.Main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.Movie
import app.beer.kinozver.utils.APP_ACTIVITY
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var movies: ArrayList<Movie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = recycler_view_movies
        adapter = MainAdapter(movies, APP_ACTIVITY)
        layoutManager = LinearLayoutManager(APP_ACTIVITY, RecyclerView.HORIZONTAL, false);
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun applyData() {
        movies = ArrayList()
        movies.add(Movie(2, "Грейхаунд", "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/f8045316-9bca-40b2-8ddb-14038bdb7c03/300x450"))
        movies.add(Movie(3, "Форпост", "https://avatars.mds.yandex.net/get-kinopoisk-image/1599028/5ac15a39-2964-497e-bb38-8299e5bc02e4/300x450"))
        movies.add(Movie(4, "Хакер", "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/2b839c56-b1d8-4ca5-be0b-e500dce9214a/300x450"))
        movies.add(Movie(5, "Третий лишний 2", "https://avatars.mds.yandex.net/get-kinopoisk-image/1704946/d6833329-4b00-4a4e-8072-277651a409de/300x450"))
        movies.add(Movie(6, "Почти знамениты", "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/0ad9e05d-1ddc-415c-af69-aa2a2edd78e2/300x450"))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_activity_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}