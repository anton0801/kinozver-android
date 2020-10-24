package app.beer.kinozver.ui.fragments.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.ui.fragments.show.MovieDetailFragment
import app.beer.kinozver.utils.downloadAndSetImage
import app.beer.kinozver.utils.replaceFragment
import kotlinx.android.synthetic.main.movie_item.view.*

class MainAdapter(val movies: List<MovieJSON>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView = itemView.movie_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieJSON: MovieJSON = movies[position]
        holder.movieImage.downloadAndSetImage(movieJSON.posterUrl)
        holder.itemView.setOnClickListener { replaceFragment(MovieDetailFragment.newInstance(movieJSON)) }
    }

}