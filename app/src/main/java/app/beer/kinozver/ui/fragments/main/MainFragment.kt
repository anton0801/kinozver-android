package app.beer.kinozver.ui.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.SectionItem
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.models.movie.MovieValue
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.ui.fragments.show.MovieDetailFragment
import app.beer.kinozver.utils.APP
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.AppRetrofitCallback
import app.beer.kinozver.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sections: ArrayList<SectionItem>
    private lateinit var movies: ArrayList<MovieJSON>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.toolbar.title = getString(R.string.app_name)
        applyData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        getMovies()
        recyclerView = recycler_view_movies
        adapter = MainAdapter(sections)
        layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun applyData() {
        movies = ArrayList()
        sections = ArrayList()
    }

    private fun setMovies(movies: ArrayList<MovieJSON>) {
        this.movies.addAll(movies)
    }

    private fun getMovies() {
        APP.getMovieApi().getMoviesAll(1).enqueue(AppRetrofitCallback<MovieValue> { _, response ->
            val movieValue = response.body()
            if (movieValue != null) {
                movies.addAll(movieValue.results)
            }
            sections.add(SectionItem("Фильмы", movies))
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_activity_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}