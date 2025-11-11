package com.example.watchtrackr.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watchtrackr.R
import com.example.watchtrackr.data.models.Movie

class MovieAdapter(
    private var items: MutableList<Movie>,
    private val onClick: (Movie) -> Unit,
    private val onRemove: (Movie) -> Unit,
    private val showRemove: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_EMPTY = 1
        private const val TAG = "MovieAdapter"
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        val btnRemove: View = view.findViewById(R.id.btnRemove)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    }

    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivEmpty: ImageView = view.findViewById(R.id.ivEmpty)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) TYPE_EMPTY else TYPE_MOVIE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_EMPTY) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_empty, parent, false)
            EmptyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            MovieViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = items[position]
            holder.tvTitle.text = movie.title
            Glide.with(holder.ivPoster.context)
                .load(movie.thumbnail)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(holder.ivPoster)

            holder.btnRemove.visibility = if (showRemove) View.VISIBLE else View.GONE

            // Click listener for the entire item
            holder.itemView.setOnClickListener {
                onClick(movie)
                Toast.makeText(holder.itemView.context, "Clicked: ${movie.title}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Movie clicked: ${movie.title}")
            }

            // Click listener for remove button
            holder.btnRemove.setOnClickListener {
                onRemove(movie)
                Toast.makeText(holder.itemView.context, "Removed: ${movie.title}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Movie removed: ${movie.title}")
            }

        } else if (holder is EmptyViewHolder) {
            // Load your drawable icon for empty state
            Glide.with(holder.ivEmpty.context)
                .load(R.drawable.no_items) // <-- your drawable
                .into(holder.ivEmpty)

            Log.d(TAG, "Empty view displayed")
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 1 else items.size
    }

    fun setItems(newItems: List<Movie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        Log.d(TAG, "Items updated. Count: ${items.size}")
    }

    fun sortByTitle() {
        items.sortBy { it.title }
        notifyDataSetChanged()
        Log.d(TAG, "Items sorted by title")
    }
}
