package com.example.watchtrackr.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watchtrackr.R
import com.example.watchtrackr.data.models.Movie

class MovieAdapter(
    private var items: MutableList<Movie>,
    private val onClick: (Movie) -> Unit,
    private val onRemove: (Movie) -> Unit,
    private val showRemove: Boolean = false
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val btnRemove: View = view.findViewById(R.id.btnRemove)  // add remove button reference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = items[position]
        holder.tvTitle.text = movie.title

        Glide.with(holder.ivPoster.context)
            .load(movie.thumbnail)
            .placeholder(R.drawable.ic_movie_placeholder)
            .into(holder.ivPoster)

        // Show/hide remove button based on flag
        holder.btnRemove.visibility = if (showRemove) View.VISIBLE else View.GONE

        // Click listeners
        holder.itemView.setOnClickListener { onClick(movie) }
        holder.btnRemove.setOnClickListener { onRemove(movie) }
    }


    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun sortByTitle() {
        items.sortBy { it.title }
        notifyDataSetChanged()
    }
}
