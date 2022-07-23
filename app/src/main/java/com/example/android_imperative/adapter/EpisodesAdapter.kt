package com.example.android_imperative.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android_imperative.R
import com.example.android_imperative.activity.DetailsActivity
import com.example.android_imperative.databinding.ItemEpisodesBinding
import com.example.android_imperative.model.Episode
import com.example.android_imperative.model.TVShow

class EpisodesAdapter(var activity: DetailsActivity, var items: ArrayList<Episode>):BaseAdapter() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_episodes, parent, false)
        return TVShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val episode: Episode = items[position]
        if (holder is TVShowViewHolder) {

            holder.binding.tvName.text = episode.name
            holder.binding.tvEpisode.text = episode.episode.toString()
            holder.binding.tvSeason.text = episode.season.toString()
            holder.binding.tvAirdate.text = episode.airDate

        }
    }

    fun setNewTVShows(episode: ArrayList<Episode>){
        items.addAll(episode)
        notifyDataSetChanged()
    }


    inner class TVShowViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemEpisodesBinding.bind(view)

    }
}