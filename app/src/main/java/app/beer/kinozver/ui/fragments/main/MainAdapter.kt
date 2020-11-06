package app.beer.kinozver.ui.fragments.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.SectionItem
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.ui.fragments.show.MovieDetailFragment
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.downloadAndSetImage
import app.beer.kinozver.utils.replaceFragment
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class MainAdapter(val sections: List<SectionItem>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieGenreName: TextView = itemView.movie_genre_name
        var childRecyclerView: RecyclerView = itemView.child_movies_recycler_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sectionItem = sections[position]

        holder.movieGenreName.text = sectionItem.movieGenreName
        val childRecyclerView = holder.childRecyclerView
        childRecyclerView.adapter = ChildRecyclerViewAdapter(sectionItem.movieList)
        childRecyclerView.layoutManager =
            LinearLayoutManager(APP_ACTIVITY, RecyclerView.HORIZONTAL, false)
    }

    class ChildRecyclerViewAdapter(val movies: ArrayList<MovieJSON>) :
        RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder>() {
        class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val movieImage: ImageView = itemView.movie_image
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item, parent, false)
            return ChildViewHolder(view)
        }

        override fun getItemCount(): Int {
            return movies.size
        }

        override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
            holder.movieImage.downloadAndSetImage(movies[position].info.posterUrl)
            holder.itemView.setOnClickListener { replaceFragment(MovieDetailFragment.newInstance(Integer.valueOf(movies[position].id)), true) }
        }
    }

}