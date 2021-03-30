package app.beer.kinozver.ui.fragments.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.section.SectionItem
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.ui.fragments.show.MovieDetailFragment
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.downloadAndSetImage
import app.beer.kinozver.utils.replaceFragment
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val sections: ArrayList<SectionItem> = ArrayList()

    fun setSections(items: List<SectionItem>) {
        sections.clear()
        sections.addAll(items)
        // notifyItemRangeInserted(0, sections.size)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieGenreName: TextView = itemView.movie_genre_name
        var childRecyclerView: RecyclerView = itemView.child_movies_recycler_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = sections.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sectionItem = sections[position]

        holder.movieGenreName.text = sectionItem.movieGenreName
        val childRecyclerView = holder.childRecyclerView
        childRecyclerView.adapter = ChildRecyclerViewAdapter(sectionItem.movies)
        childRecyclerView.layoutManager =
            LinearLayoutManager(APP_ACTIVITY, RecyclerView.HORIZONTAL, false)
    }

    class ChildRecyclerViewAdapter(val movies: ArrayList<MovieJSON>) :
        RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder>() {

        class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val movieImage: ImageView = itemView.movie_item_image
            val movieContinueTime: TextView = itemView.movie_continue_time
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
            val movie = movies[position]

            with(movie) {
                holder.movieImage.downloadAndSetImage(posterUrl)

                if (movie.continueWatchTime > 0) {
                    val hours = continueWatchTime / 3600
                    val minutes = continueWatchTime / 60 % 60
                    holder.movieContinueTime.visibility = View.VISIBLE
                    holder.movieContinueTime.text =
                        if (hours > 0) "$hours ч. $minutes мин." else "$minutes мин."
                }

                val movieId = if (movie_id != "") movie_id else idM

                holder.itemView.setOnClickListener {
                    replaceFragment(
                        MovieDetailFragment.newInstance(
                            Integer.valueOf(movieId)
                        ), true
                    )
                }
            }
        }
    }

}