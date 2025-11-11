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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_EMPTY = 1
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
            holder.itemView.setOnClickListener { onClick(movie) }
            holder.btnRemove.setOnClickListener { onRemove(movie) }
        } else if (holder is EmptyViewHolder) {
            // Load your drawable icon for empty state
            Glide.with(holder.ivEmpty.context)
                .load(R.drawable.no_items) // <-- your drawable
                .into(holder.ivEmpty)
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) 1 else items.size
    }

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





