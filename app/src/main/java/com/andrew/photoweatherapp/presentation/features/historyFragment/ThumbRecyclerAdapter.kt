package com.andrew.photoweatherapp.presentation.features.historyFragment

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.andrew.photoweatherapp.R

class ThumbHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val thumbPhoto by lazy { itemView.findViewById<ImageView>(R.id.thumb_imageView) }
    fun bind(path: String) {
        thumbPhoto.setImageURI(Uri.parse(path))
    }
}

class ThumbRecyclerAdapter(
    private val items: MutableList<String> = mutableListOf(),
    private val onViewClicked: (String) -> Unit
) : RecyclerView.Adapter<ThumbHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.itme_thumb, parent, false)
        .let { ThumbHolder(it) }

    override fun onBindViewHolder(holder: ThumbHolder, position: Int) = items[position].let { path ->
        holder.bind(path)
        holder.itemView.setOnClickListener { onViewClicked(path) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}