package app.beer.kinozver.ui.fragments.Main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.Movie
import app.beer.kinozver.ui.fragments.show.MovieDetailFragment
import app.beer.kinozver.utils.replaceFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class MainAdapter(val movies: List<Movie>, val context: Context) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView = itemView.movie_image
        var movieName: TextView = itemView.movie_name
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
        val movie: Movie = movies[position]
        Picasso.get()
            .load(movie.posterUrl)
            .fit()
            .into(holder.movieImage)
        holder.movieName.text = movie.name
        holder.itemView.setOnClickListener { replaceFragment(MovieDetailFragment.newInstance(movie)) }
    }

}