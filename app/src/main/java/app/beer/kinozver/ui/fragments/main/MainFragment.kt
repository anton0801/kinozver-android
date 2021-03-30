package app.beer.kinozver.ui.fragments.main

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.database.USER
import app.beer.kinozver.models.section.SectionItem
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.models.section.SectionResponse
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.utils.APP
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.AppRetrofitCallback
import app.beer.kinozver.utils.SharedManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sections: ArrayList<SectionItem>
    private lateinit var movies: ArrayList<MovieJSON>

    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.toolbar.title = getString(R.string.app_name)
        APP_ACTIVITY.bottomNavigationView.visibility = View.VISIBLE
        progressBar = progress_bar
        applyData()
        initRecyclerView()
        getMovies()
    }

    private fun initRecyclerView() {
        recyclerView = recycler_view_movies
        adapter = MainAdapter()
        layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun applyData() {
        movies = ArrayList()
        sections = ArrayList()
    }

    private fun getMovies() {
        progressBar.visibility = View.VISIBLE
        APP_ACTIVITY.viewModel.getSectionedMovies(USER.id).observe(APP_ACTIVITY, Observer {
            sections.clear()
            sections.addAll(it)
            adapter.setSections(sections)
            progressBar.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_activity_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}